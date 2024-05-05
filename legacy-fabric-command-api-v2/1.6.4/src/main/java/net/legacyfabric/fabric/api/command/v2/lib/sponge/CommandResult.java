/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.api.command.v2.lib.sponge;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the result of a command in Sponge.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CommandResult {
	private static final CommandResult EMPTY = builder().build();
	private static final CommandResult SUCCESS = builder().successCount(1).build();
	private final Optional<Integer> successCount;
	private final Optional<Integer> affectedBlocks;
	private final Optional<Integer> affectedEntities;
	private final Optional<Integer> affectedItems;
	private final Optional<Integer> queryResult;

	/**
	 * Returns a {@link Builder}.
	 *
	 * @return A new command result builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Returns a new {@link CommandResult} indicating that a command was
	 * processed.
	 *
	 * @return The command result
	 */
	public static CommandResult empty() {
		return EMPTY;
	}

	/**
	 * Returns a result indicating the command was processed with a single
	 * success.
	 *
	 * @return The result
	 */
	public static CommandResult success() {
		return SUCCESS;
	}

	/**
	 * Returns a result indicating the command was processed with a single
	 * success.
	 *
	 * @param count The success count
	 * @return The result
	 */
	public static CommandResult successCount(int count) {
		return builder().successCount(count).build();
	}

	/**
	 * Returns a result indicating the command was processed with an
	 * amount of affected blocks.
	 *
	 * @param count The amount of blocks affected
	 * @return The result
	 */
	public static CommandResult affectedBlocks(int count) {
		return builder().affectedBlocks(count).build();
	}

	/**
	 * Returns a result indicating the command was processed with an
	 * amount of affected entities.
	 *
	 * @param count The amount of entities affected
	 * @return The result
	 */
	public static CommandResult affectedEntities(int count) {
		return builder().affectedEntities(count).build();
	}

	/**
	 * Returns a result indicating the command was processed with an
	 * amount of affected items.
	 *
	 * @param count The amount of items affected
	 * @return The result
	 */
	public static CommandResult affectedItems(int count) {
		return builder().affectedItems(count).build();
	}

	/**
	 * Returns a result indicating the command was processed with an
	 * amount of queries.
	 *
	 * @param count The amount of queries
	 * @return The result
	 */
	public static CommandResult queryResult(int count) {
		return builder().queryResult(count).build();
	}

	/**
	 * Constructs a new command result.
	 *
	 * @param successCount     The success count
	 * @param affectedBlocks   The number of affected blocks
	 * @param affectedEntities The number of affected entities
	 * @param affectedItems    The number of affected items
	 * @param queryResult      The query result
	 */
	CommandResult(@Nullable Integer successCount, @Nullable Integer affectedBlocks, @Nullable Integer affectedEntities, @Nullable Integer affectedItems, @Nullable Integer queryResult) {
		this.successCount = Optional.ofNullable(successCount);
		this.affectedBlocks = Optional.ofNullable(affectedBlocks);
		this.affectedEntities = Optional.ofNullable(affectedEntities);
		this.affectedItems = Optional.ofNullable(affectedItems);
		this.queryResult = Optional.ofNullable(queryResult);
	}

	/**
	 * Gets the success count of the command.
	 *
	 * @return The success count of the command
	 */
	public Optional<Integer> getSuccessCount() {
		return this.successCount;
	}

	/**
	 * Gets the number of blocks affected by the command.
	 *
	 * @return The number of blocks affected by the command, if such a count
	 * exists
	 */
	public Optional<Integer> getAffectedBlocks() {
		return this.affectedBlocks;
	}

	/**
	 * Gets the number of entities affected by the command.
	 *
	 * @return The number of entities affected by the command, if such a count
	 * exists
	 */
	public Optional<Integer> getAffectedEntities() {
		return this.affectedEntities;
	}

	/**
	 * Gets the number of items affected by the command.
	 *
	 * @return The number of items affected by the command, if such a count
	 * exists
	 */
	public Optional<Integer> getAffectedItems() {
		return this.affectedItems;
	}

	/**
	 * Gets the query result of the command, e.g. the time of the day,
	 * an amount of money or a player's amount of XP.
	 *
	 * @return The query result of the command, if one exists
	 */
	public Optional<Integer> getQueryResult() {
		return this.queryResult;
	}

	/**
	 * A builder for {@link CommandResult}s.
	 */
	public static class Builder {
		@Nullable
		private Integer successCount;
		@Nullable
		private Integer affectedBlocks;
		@Nullable
		private Integer affectedEntities;
		@Nullable
		private Integer affectedItems;
		@Nullable
		private Integer queryResult;

		Builder() {
		}

		/**
		 * Sets if the command has been processed.
		 *
		 * @param successCount If the command has been processed
		 * @return This builder, for chaining
		 */
		public Builder successCount(@Nullable Integer successCount) {
			this.successCount = successCount;
			return this;
		}

		/**
		 * Sets the amount of blocks affected by the command.
		 *
		 * @param affectedBlocks The amount of blocks affected by the command
		 * @return This builder, for chaining
		 */
		public Builder affectedBlocks(@Nullable Integer affectedBlocks) {
			this.affectedBlocks = affectedBlocks;
			return this;
		}

		/**
		 * Sets the amount of entities affected by the command.
		 *
		 * @param affectedEntities The amount of entities affected by the
		 *                         command
		 * @return This builder, for chaining
		 */
		public Builder affectedEntities(@Nullable Integer affectedEntities) {
			this.affectedEntities = affectedEntities;
			return this;
		}

		/**
		 * Sets the amount of items affected by the command.
		 *
		 * @param affectedItems The amount of items affected by the command
		 * @return This builder, for chaining
		 */
		public Builder affectedItems(@Nullable Integer affectedItems) {
			this.affectedItems = affectedItems;
			return this;
		}

		/**
		 * Sets the query result of the command, e.g. the time of the day,
		 * an amount of money or a player's amount of XP.
		 *
		 * @param queryResult The query result of the command
		 * @return This builder, for chaining
		 */
		public Builder queryResult(@Nullable Integer queryResult) {
			this.queryResult = queryResult;
			return this;
		}

		/**
		 * Builds the {@link CommandResult}.
		 *
		 * @return A CommandResult with the specified settings
		 */
		public CommandResult build() {
			return new CommandResult(this.successCount, this.affectedBlocks, this.affectedEntities, this.affectedItems, this.queryResult);
		}
	}
}
