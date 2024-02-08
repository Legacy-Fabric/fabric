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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.spec;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandCallable;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandPermissionException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ChildCommandElementExecutor;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing.InputTokenizer;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.api.util.Location;

/**
 * Specification for how command arguments should be parsed.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class CommandSpec implements CommandCallable {
	private final CommandElement args;
	private final CommandExecutor executor;
	private final Optional<Text> description;
	private final Optional<Text> extendedDescription;
	@Nullable
	private final String permission;
	private final InputTokenizer argumentParser;

	CommandSpec(CommandElement args, CommandExecutor executor, @Nullable Text description, @Nullable Text extendedDescription,
				@Nullable String permission, InputTokenizer parser) {
		this.args = args;
		this.executor = executor;
		this.permission = permission;
		this.description = Optional.ofNullable(description);
		this.extendedDescription = Optional.ofNullable(extendedDescription);
		this.argumentParser = parser;
	}

	/**
	 * Return a new builder for a CommandSpec.
	 *
	 * @return a new builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for command specs.
	 */
	public static final class Builder {
		private static final CommandElement DEFAULT_ARG = GenericArguments.none();
		private CommandElement args = DEFAULT_ARG;
		@Nullable
		private Text description;
		@Nullable
		private Text extendedDescription;
		@Nullable
		private String permission;
		@Nullable
		private CommandExecutor executor;
		@Nullable
		private Map<List<String>, CommandCallable> childCommandMap;
		private boolean childCommandFallback = true;
		private InputTokenizer argumentParser = InputTokenizer.quotedStrings(false);

		Builder() {
		}

		/**
		 * Sets the permission that will be checked before using this command.
		 *
		 * @param permission The permission to check
		 * @return this
		 */
		public Builder permission(String permission) {
			this.permission = permission;
			return this;
		}

		/**
		 * Sets the callback that will handle this command's execution.
		 *
		 * @param executor The executor that will be called with this command's
		 *                 parsed arguments
		 * @return this
		 */
		public Builder executor(CommandExecutor executor) {
			Preconditions.checkNotNull(executor, "executor");
			this.executor = executor;
			return this;
		}

		/**
		 * Adds more child arguments for this command.
		 * If an executor or arguments are set, they are used as fallbacks.
		 *
		 * @param children The children to use
		 * @return this
		 */
		public Builder children(Map<List<String>, ? extends CommandCallable> children) {
			Preconditions.checkNotNull(children, "children");

			if (this.childCommandMap == null) {
				this.childCommandMap = new HashMap<>();
			}

			this.childCommandMap.putAll(children);
			return this;
		}

		/**
		 * Add a single child command to this command.
		 *
		 * @param child   The child to add
		 * @param aliases Aliases to make the child available under. First
		 *                one is primary and is the only one guaranteed to be listed in
		 *                usage outputs.
		 * @return this
		 */
		public Builder child(CommandCallable child, String... aliases) {
			if (this.childCommandMap == null) {
				this.childCommandMap = new HashMap<>();
			}

			this.childCommandMap.put(ImmutableList.copyOf(aliases), child);
			return this;
		}

		/**
		 * Add a single child command to this command.
		 *
		 * @param child   The child to add.
		 * @param aliases Aliases to make the child available under. First
		 *                one is primary and is the only one guaranteed to be listed in
		 *                usage outputs.
		 * @return this
		 */
		public Builder child(CommandCallable child, Collection<String> aliases) {
			if (this.childCommandMap == null) {
				this.childCommandMap = new HashMap<>();
			}

			this.childCommandMap.put(ImmutableList.copyOf(aliases), child);
			return this;
		}

		/**
		 * A short, one-line description of this command's purpose.
		 *
		 * @param description The description to set
		 * @return this
		 */
		public Builder description(@Nullable Text description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets an extended description to use in longer help listings for this
		 * command. Will be appended to the short description and the command's
		 * usage.
		 *
		 * @param extendedDescription The description to set
		 * @return this
		 */
		public Builder extendedDescription(@Nullable Text extendedDescription) {
			this.extendedDescription = extendedDescription;
			return this;
		}

		/**
		 * If a child command is selected but fails to parse arguments passed to
		 * it, the following determines the behavior.
		 *
		 * <ul>
		 *     <li>If this is set to <strong>false</strong>, this command (the
		 *     parent) will not attempt to parse the command, and will send back
		 *     the error from the child.</li>
		 *     <li>If this is set to <strong>true</strong>, the error from the
		 *     child will simply be discarded, and the parent command will
		 *     execute.</li>
		 * </ul>
		 *
		 * <p>The default for this is <strong>true</strong>, which emulates the
		 * behavior from previous API revisions.</p>
		 *
		 * @param childCommandFallback Whether to fallback on argument parse
		 *                             failure
		 * @return this
		 */
		public Builder childArgumentParseExceptionFallback(boolean childCommandFallback) {
			this.childCommandFallback = childCommandFallback;
			return this;
		}

		/**
		 * Sets the argument specification for this command. Generally, for a
		 * multi-argument command the {@link GenericArguments#seq(CommandElement...)}
		 * method is used to parse a sequence of args.
		 *
		 * @param args The arguments object to use
		 * @return this
		 * @see GenericArguments
		 */
		public Builder arguments(CommandElement args) {
			Preconditions.checkNotNull(args, "args");
			this.args = GenericArguments.seq(args);
			return this;
		}

		/**
		 * Sets the argument specification for this command. This method accepts
		 * a sequence of arguments. This is equivalent to calling {@code
		 * arguments(seq(args))}.
		 *
		 * @param args The arguments object to use
		 * @return this
		 * @see GenericArguments
		 */
		public Builder arguments(CommandElement... args) {
			Preconditions.checkNotNull(args, "args");
			this.args = GenericArguments.seq(args);
			return this;
		}

		/**
		 * Sets the input tokenizer to be used to convert input from a string
		 * into a list of argument tokens.
		 *
		 * @param parser The parser to use
		 * @return this
		 * @see InputTokenizer for common input parser implementations
		 */
		public Builder inputTokenizer(InputTokenizer parser) {
			Preconditions.checkNotNull(parser, "parser");
			this.argumentParser = parser;
			return this;
		}

		/**
		 * Create a new {@link CommandSpec} based on the data provided in this
		 * builder.
		 *
		 * @return the new spec
		 */
		public CommandSpec build() {
			if (this.childCommandMap == null || this.childCommandMap.isEmpty()) {
				Preconditions.checkNotNull(this.executor, "An executor is required");
			} else if (this.executor == null) {
				ChildCommandElementExecutor childCommandElementExecutor =
						this.registerInDispatcher(new ChildCommandElementExecutor(null, null, false));
				if (this.args == DEFAULT_ARG) {
					this.arguments(childCommandElementExecutor);
				} else {
					this.arguments(this.args, childCommandElementExecutor);
				}
			} else {
				this.arguments(this.registerInDispatcher(new ChildCommandElementExecutor(this.executor, this.args, this.childCommandFallback)));
			}

			return new CommandSpec(this.args, this.executor, this.description, this.extendedDescription, this.permission,
					this.argumentParser);
		}

		@SuppressWarnings({"ConstantConditions"})
		private ChildCommandElementExecutor registerInDispatcher(ChildCommandElementExecutor childDispatcher) {
			for (Map.Entry<List<String>, ? extends CommandCallable> spec : this.childCommandMap.entrySet()) {
				childDispatcher.register(spec.getValue(), spec.getKey());
			}

			this.executor(childDispatcher);
			return childDispatcher;
		}
	}

	/**
	 * Check the relevant permission for this command with the provided source,
	 * throwing an exception if the source does not have permission to use
	 * the command.
	 *
	 * @param source The source to check
	 * @throws CommandException if the source does not have permission
	 */
	public void checkPermission(PermissibleCommandSource source) throws CommandException {
		Preconditions.checkNotNull(source, "source");

		if (!this.testPermission(source)) {
			throw new CommandPermissionException();
		}
	}

	/**
	 * Process this command with existing arguments and context objects.
	 *
	 * @param source  The source to populate the context with
	 * @param args    The arguments to process with
	 * @param context The context to put data in
	 * @throws ArgumentParseException if an invalid argument is provided
	 */
	public void populateContext(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		this.args.parse(source, args, context);

		if (args.hasNext()) {
			args.next();
			throw args.createError(new LiteralText("Too many arguments!"));
		}
	}

	/**
	 * Return tab completion results using the existing parsed arguments and
	 * context. Primarily useful when including a subcommand in an existing
	 * specification.
	 *
	 * @param source  The source to parse arguments for
	 * @param args    The arguments object
	 * @param context The context object
	 * @return possible completions, or an empty list if none
	 */
	public List<String> complete(PermissibleCommandSource source, CommandArgs args, CommandContext context) {
		Preconditions.checkNotNull(source, "source");
		List<String> ret = this.args.complete(source, args, context);
		return ret == null ? ImmutableList.of() : ImmutableList.copyOf(ret);
	}

	/**
	 * Gets the active executor for this command. Generally not a good idea to
	 * call this directly, unless you are handling arg parsing specially
	 *
	 * @return The active executor for this command
	 */
	public CommandExecutor getExecutor() {
		return this.executor;
	}

	/**
	 * Gets the active input tokenizer used for this command.
	 *
	 * @return This command's input tokenizer
	 */
	public InputTokenizer getInputTokenizer() {
		return this.argumentParser;
	}

	@Override
	public CommandResult process(PermissibleCommandSource source, String arguments) throws CommandException {
		this.checkPermission(source);
		final CommandArgs args = new CommandArgs(arguments, this.getInputTokenizer().tokenize(arguments, false));
		final CommandContext context = new CommandContext();
		this.populateContext(source, args, context);
		return this.getExecutor().execute(source, context);
	}

	@Override
	public List<String> getSuggestions(PermissibleCommandSource source, String arguments, @Nullable Location<World> targetPos) throws CommandException {
		CommandArgs args = new CommandArgs(arguments, this.getInputTokenizer().tokenize(arguments, true));
		CommandContext ctx = new CommandContext();

		if (targetPos != null) {
			ctx.putArg(CommandContext.TARGET_BLOCK_ARG, targetPos);
		}

		ctx.putArg(CommandContext.TAB_COMPLETION, true);
		return this.complete(source, args, ctx);
	}

	@Override
	public boolean testPermission(PermissibleCommandSource source) {
		return source.hasPermission(this.permission);
	}

	/**
	 * Gets a short, one-line description used with this command if any is
	 * present.
	 *
	 * @return the short description.
	 */
	@Override
	public Optional<Text> getShortDescription(PermissibleCommandSource source) {
		return this.description;
	}

	/**
	 * Gets the extended description used with this command if any is present.
	 *
	 * @param source The source to get the description for
	 * @return the extended description.
	 */
	public Optional<Text> getExtendedDescription(PermissibleCommandSource source) {
		return this.extendedDescription;
	}

	/**
	 * Gets the usage for this command appropriate for the provided command
	 * source.
	 *
	 * @param source The source
	 * @return the usage for the source
	 */
	@Override
	public Text getUsage(PermissibleCommandSource source) {
		Preconditions.checkNotNull(source, "source");
		return this.args.getUsage(source);
	}

	/**
	 * Return a longer description for this command. This description is
	 * composed of at least all present of the short description, the usage
	 * statement, and the extended description
	 *
	 * @param source The source to get the extended description for
	 * @return the extended description
	 */
	@Override
	public Optional<Text> getHelp(PermissibleCommandSource source) {
		Preconditions.checkNotNull(source, "source");
		StringBuilder builder = new StringBuilder();
		this.getShortDescription(source).ifPresent((a) -> builder.append(a.asUnformattedString()).append("\n"));
		builder.append(this.getUsage(source));
		this.getExtendedDescription(source).ifPresent((a) -> builder.append(a.asUnformattedString()).append("\n"));
		return Optional.of(new LiteralText(builder.toString()));
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		CommandSpec that = (CommandSpec) o;
		return Objects.equal(this.args, that.args)
				&& Objects.equal(this.executor, that.executor)
				&& Objects.equal(this.description, that.description)
				&& Objects.equal(this.extendedDescription, that.extendedDescription)
				&& Objects.equal(this.permission, that.permission)
				&& Objects.equal(this.argumentParser, that.argumentParser);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.args, this.executor, this.description, this.extendedDescription, this.permission, this.argumentParser);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("args", this.args)
				.add("executor", this.executor)
				.add("description", this.description)
				.add("extendedDescription", this.extendedDescription)
				.add("permission", this.permission)
				.add("argumentParser", this.argumentParser)
				.toString();
	}
}
