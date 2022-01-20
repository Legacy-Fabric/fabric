/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.impl.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher.Disambiguator;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher.SimpleDispatcher;
import net.legacyfabric.fabric.api.util.Location;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandPermissionException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.InvocationCommandException;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class CommandManagerImpl implements CommandManager {
	private static final Logger LOGGER = LogManager.getLogger("Fabric Command Manager");
	private final Object lock = new Object();
	private final SimpleDispatcher dispatcher;
	private final List<CommandMapping> mappings = Lists.newArrayList();

	public CommandManagerImpl(Disambiguator disambiguator) {
		this.dispatcher = new SimpleDispatcher(disambiguator);
	}

	@Override
	public Optional<CommandMapping> register(CommandCallable callable, String... alias) {
		return this.register(callable, Arrays.asList(alias));
	}

	@Override
	public Optional<CommandMapping> register(CommandCallable callable, List<String> aliases) {
		return this.register(callable, aliases, Function.identity());
	}

	@Override
	public Optional<CommandMapping> register(CommandCallable callable, List<String> aliases, Function<List<String>, List<String>> callback) {
		synchronized (this.lock) {
			return this.dispatcher.register(callable, aliases, callback).map(mapping -> {
				this.mappings.add(mapping);
				return mapping;
			});
		}
	}

	@Override
	public Optional<CommandMapping> removeMapping(CommandMapping mapping) {
		synchronized (this.lock) {
			return this.dispatcher.removeMapping(mapping).map(commandMapping -> {
				this.mappings.remove(commandMapping);
				return commandMapping;
			});
		}
	}

	@Override
	public int size() {
		synchronized (this.lock) {
			return this.dispatcher.size();
		}
	}

	@Override
	public CommandResult process(PermissibleCommandSource source, String command) {
		final String[] argSplit = command.split(" ", 2);

		try {
			try {
				this.dispatcher.process(source, command);
			} catch (InvocationCommandException e) {
				if (e.getCause() != null) {
					throw e.getCause();
				}
			} catch (CommandPermissionException e) {
				if (e.getText() != null) {
					source.sendMessage(CommandMessageFormatting.error(e.getText()));
				}
			} catch (CommandException e) {
				Text text = e.getText();

				if (text != null) {
					source.sendMessage(CommandMessageFormatting.error(text));
				}

				if (e.shouldIncludeUsage()) {
					Optional<CommandMapping> mapping = this.dispatcher.get(argSplit[0], source);

					if (mapping.isPresent()) {
						Text usage;

						if (e instanceof ArgumentParseException.WithUsage) {
							usage = ((ArgumentParseException.WithUsage) e).getUsage();
						} else {
							usage = mapping.get().getCallable().getUsage(source);
						}

						source.sendMessage(CommandMessageFormatting.error(new LiteralText(String.format("Usage: /%s %s", argSplit[0], usage))));
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error("An unexpected error happened executing a command");
			t.printStackTrace();

			if (t instanceof Error) {
				throw (Error) t;
			}

			Text message = CommandMessageFormatting.error(new LiteralText("An unexpected error happened executing the command"));
			message.setStyle(message.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Stacktrace: \n" + Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"))))));
			source.sendMessage(message);
		}

		return CommandResult.empty();
	}

	@Override
	public List<String> getSuggestions(PermissibleCommandSource source, String arguments, @Nullable Location<World> targetPosition) {
		try {
			final String[] argSplit = arguments.split(" ", 2);
			return Lists.newArrayList(this.dispatcher.getSuggestions(source, arguments, targetPosition));
		} catch (CommandException e) {
			source.sendMessage(CommandMessageFormatting.error(new LiteralText(String.format("Error getting suggestions: %s", e.getText().getString()))));
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error occured while tab completing '%s'", arguments), e);
		}
	}

	@Override
	public boolean testPermission(PermissibleCommandSource source) {
		synchronized (this.lock) {
			return this.dispatcher.testPermission(source);
		}
	}

	@Override
	public Optional<Text> getShortDescription(PermissibleCommandSource source) {
		synchronized (this.lock) {
			return this.dispatcher.getShortDescription(source);
		}
	}

	@Override
	public Optional<Text> getHelp(PermissibleCommandSource source) {
		synchronized (this.lock) {
			return this.dispatcher.getHelp(source);
		}
	}

	@Override
	public Text getUsage(PermissibleCommandSource source) {
		synchronized (this.lock) {
			return this.dispatcher.getUsage(source);
		}
	}

	@Override
	public Set<? extends CommandMapping> getCommands() {
		synchronized (this.lock) {
			return this.dispatcher.getCommands();
		}
	}

	@Override
	public Set<String> getPrimaryAliases() {
		synchronized (this.lock) {
			return this.dispatcher.getPrimaryAliases();
		}
	}

	@Override
	public Set<String> getAliases() {
		synchronized (this.lock) {
			return this.dispatcher.getAliases();
		}
	}

	@Override
	public Optional<? extends CommandMapping> get(String alias) {
		synchronized (this.lock) {
			return this.dispatcher.get(alias);
		}
	}

	@Override
	public Optional<? extends CommandMapping> get(String alias, @Nullable PermissibleCommandSource source) {
		synchronized (this.lock) {
			return this.dispatcher.get(alias, source);
		}
	}

	@Override
	public Set<? extends CommandMapping> getAll(String alias) {
		synchronized (this.lock) {
			return this.dispatcher.getAll(alias);
		}
	}

	protected List<CommandMapping> getMappings() {
		return this.mappings;
	}

	@Override
	public Multimap<String, CommandMapping> getAll() {
		synchronized (this.lock) {
			return this.dispatcher.getAll();
		}
	}

	@Override
	public boolean containsAlias(String alias) {
		synchronized (this.lock) {
			return this.dispatcher.containsAlias(alias);
		}
	}

	@Override
	public boolean containsMapping(CommandMapping mapping) {
		synchronized (this.lock) {
			return this.dispatcher.containsMapping(mapping);
		}
	}
}
