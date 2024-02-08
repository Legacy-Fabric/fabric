/*
 * Copyright (c) 2020 - 2022 Legacy Fabric
 * Copyright (c) 2016 - 2022 FabricMC
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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMessageFormatting;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.dispatcher.SimpleDispatcher;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandExecutor;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

public class ChildCommandElementExecutor extends CommandElement implements CommandExecutor {
	private static final AtomicInteger COUNTER = new AtomicInteger();
	private static final CommandElement NONE = GenericArguments.none();

	@Nullable
	private final CommandExecutor fallbackExecutor;
	@Nullable
	private final CommandElement fallbackElements;
	private final SimpleDispatcher dispatcher = new SimpleDispatcher(SimpleDispatcher.FIRST_DISAMBIGUATOR);
	private final boolean fallbackOnFail;

	/**
	 * Create a new combined argument element and executor to handle the
	 * parsing and execution of child commands.
	 *
	 * @param fallbackExecutor The executor to execute if the child command
	 *                         has been marked optional (Generally when this is wrapped in a
	 *                         {@link GenericArguments#optional(CommandElement)}
	 * @param fallbackElements The alternate {@link CommandElement}s that should
	 *                         be parsed if a child element fails to be parsed
	 * @param fallbackOnFail   If true, then if a child command cannot parse the
	 *                         elements, the exception is discarded and the parent command attempts
	 *                         to parse the elements. If false, a child command will not pass
	 *                         control back to the parent, displaying its own exception message
	 */
	public ChildCommandElementExecutor(@Nullable CommandExecutor fallbackExecutor, @Nullable CommandElement fallbackElements, boolean fallbackOnFail) {
		super(new LiteralText("child" + COUNTER.getAndIncrement()));
		this.fallbackExecutor = fallbackExecutor;
		this.fallbackElements = NONE == fallbackElements ? null : fallbackElements;
		this.fallbackOnFail = fallbackOnFail;
	}

	/**
	 * Register a child command for a given set of aliases.
	 *
	 * @param callable The command to register
	 * @param aliases  The aliases to register it as
	 * @return The child command's mapping, if present
	 */
	public Optional<CommandMapping> register(CommandCallable callable, List<String> aliases) {
		return this.dispatcher.register(callable, aliases);
	}

	/**
	 * Register a child command for a given set of aliases.
	 *
	 * @param callable The command to register
	 * @param aliases  The aliases to register it as
	 * @return The child command's mapping, if present
	 */
	public Optional<CommandMapping> register(CommandCallable callable, String... aliases) {
		return this.dispatcher.register(callable, aliases);
	}

	@Override
	public List<String> complete(final PermissibleCommandSource src, CommandArgs args, CommandContext context) {
		List<String> completions = Lists.newArrayList();

		if (this.fallbackElements != null) {
			CommandArgs.Snapshot state = args.getSnapshot();
			completions.addAll(this.fallbackElements.complete(src, args, context));
			args.applySnapshot(state);
		}

		final Optional<String> commandComponent = args.nextIfPresent();

		if (!commandComponent.isPresent()) {
			return ImmutableList.copyOf(this.filterCommands(src));
		}

		if (args.hasNext()) {
			Optional<CommandMapping> child = this.dispatcher.get(commandComponent.get(), src);

			if (!child.isPresent()) {
				return ImmutableList.of();
			}

			if (child.get().getCallable() instanceof CommandSpec) {
				return ((CommandSpec) child.get().getCallable()).complete(src, args, context);
			}

			args.nextIfPresent();
			final String arguments = args.getRaw().substring(args.getRawPosition());

			while (args.hasNext()) {
				args.nextIfPresent();
			}

			try {
				return child.get()
						.getCallable()
						.getSuggestions(src, arguments, context.<Location<World>>getOne(CommandContext.TARGET_BLOCK_ARG).orElse(null));
			} catch (CommandException e) {
				Text eText = e.getText();

				if (eText != null) {
					src.sendMessage(CommandMessageFormatting.error(eText));
				}

				return ImmutableList.of();
			}
		}

		completions.addAll(this.filterCommands(src).stream()
				.filter(((input) -> {
					String test = commandComponent.get();
					return input.toLowerCase().startsWith(test.toLowerCase());
				}))
				.collect(Collectors.toList()));
		return completions;
	}

	private Set<String> filterCommands(final PermissibleCommandSource src) {
		return Multimaps.filterValues(
				this.dispatcher.getAll(),
				(input) ->
						input != null && input.getCallable().testPermission(src)
		).keys().elementSet();
	}

	@Override
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		if (this.fallbackExecutor != null && !args.hasNext()) {
			if (this.fallbackElements != null) {
				// there might be optionals to take account of that would parse this successfully.
				this.fallbackElements.parse(source, args, context);
			}

			return; // execute the fallback regardless in this scenario.
		}

		CommandArgs.Snapshot state = args.getSnapshot();
		final String key = args.next();
		Optional<CommandMapping> optionalCommandMapping = this.dispatcher.get(key, source).map(Function.identity());

		if (optionalCommandMapping.isPresent()) {
			final CommandMapping mapping = optionalCommandMapping.get();

			try {
				if ((mapping.getCallable() instanceof CommandSpec)) {
					CommandSpec spec = ((CommandSpec) mapping.getCallable());
					spec.populateContext(source, args, context);
				} else {
					if (args.hasNext()) {
						args.next();
						context.putArg(this.getUntranslatedKey() + "_args", args.getRaw().substring(args.getRawPosition()));
					}

					while (args.hasNext()) {
						args.next();
					}
				}

				// Success, add to context now so that we don't execute the wrong executor in the first place.
				context.putArg(this.getUntranslatedKey(), mapping);
			} catch (ArgumentParseException ex) {
				// If we get here, fallback to the elements, if they exist.
				args.applySnapshot(state);

				if (this.fallbackOnFail && this.fallbackElements != null) {
					this.fallbackElements.parse(source, args, context);
					return;
				}

				// Get the usage
				args.next();

				if (ex instanceof ArgumentParseException.WithUsage) {
					// This indicates a previous child failed, so we just prepend our child
					throw new ArgumentParseException.WithUsage(ex, new LiteralText(key + " " + ((ArgumentParseException.WithUsage) ex).getUsage()));
				}

				throw new ArgumentParseException.WithUsage(ex, new LiteralText(key + " " + mapping.getCallable().getUsage(source)));
			}
		} else {
			// Not a child, so let's continue with the fallback.
			if (this.fallbackExecutor != null && this.fallbackElements != null) {
				args.applySnapshot(state);
				this.fallbackElements.parse(source, args, context);
			} else {
				// If we have no elements to parse, then we throw this error - this is the only element
				// so specifying it implicitly means we have a child command to execute.
				throw args.createError(new LiteralText(String.format("Input command %s was not a valid subcommand!", key)));
			}
		}
	}

	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return null;
	}

	@Override
	public CommandResult execute(PermissibleCommandSource src, CommandContext args) throws CommandException {
		CommandMapping mapping = args.<CommandMapping>getOne(this.getUntranslatedKey()).orElse(null);

		if (mapping == null) {
			if (this.fallbackExecutor == null) {
				throw new CommandException(new LiteralText(String.format("Invalid subcommand state -- no more than one mapping may be provided for child arg %s", this.getKey())));
			}

			return this.fallbackExecutor.execute(src, args);
		}

		if (mapping.getCallable() instanceof CommandSpec) {
			CommandSpec spec = ((CommandSpec) mapping.getCallable());
			spec.checkPermission(src);
			return spec.getExecutor().execute(src, args);
		}

		final String arguments = args.<String>getOne(this.getUntranslatedKey() + "_args").orElse("");
		return mapping.getCallable().process(src, arguments);
	}

	@Override
	public Text getUsage(PermissibleCommandSource src) {
		Text usage = this.dispatcher.getUsage(src);

		if (this.fallbackElements == null) {
			return usage;
		}

		Text elementUsage = this.fallbackElements.getUsage(src);

		if (elementUsage.computeValue().isEmpty()) {
			return usage;
		}

		return new LiteralText("").append(usage).append(CommandMessageFormatting.PIPE_TEXT).append(elementUsage);
	}
}
