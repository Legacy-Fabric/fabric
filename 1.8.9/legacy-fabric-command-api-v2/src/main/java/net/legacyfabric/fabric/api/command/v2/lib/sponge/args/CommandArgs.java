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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing.SingleArg;

/**
 * Holder for command arguments.
 */
public final class CommandArgs {
	private final String rawInput;
	private final List<SingleArg> args;
	private int index = -1;

	/**
	 * Create a new CommandArgs instance with the given raw input and arguments.
	 *
	 * @param rawInput Raw input
	 * @param args     Arguments extracted from the raw input
	 */
	public CommandArgs(String rawInput, List<SingleArg> args) {
		this.rawInput = rawInput;
		this.args = new ArrayList<>(args);
	}

	/**
	 * Return whether more arguments remain to be read.
	 *
	 * @return Whether more arguments remain
	 */
	public boolean hasNext() {
		return this.index + 1 < this.args.size();
	}

	/**
	 * Returns true if:
	 *
	 * <ul>
	 *     <li>The next element returned by {@link #next()} is the last</li>
	 *     <li>{@link #hasNext()} is false</li>
	 * </ul>
	 * Else returns false.
	 *
	 * @return True if a completion can occur
	 */
	public boolean canComplete() {
		return this.index + 2 >= this.args.size();
	}

	/**
	 * Try to read the next argument without advancing the current index.
	 *
	 * @return The next argument
	 * @throws ArgumentParseException if not enough arguments are present
	 */
	public String peek() throws ArgumentParseException {
		if (!this.hasNext()) {
			throw this.createError(new LiteralText("Not enough arguments!"));
		}

		return this.args.get(this.index + 1).getValue();
	}

	/**
	 * Try to read the next argument, advancing the current index if successful.
	 *
	 * @return The next argument
	 * @throws ArgumentParseException if not enough arguments are present
	 */
	public String next() throws ArgumentParseException {
		if (!this.hasNext()) {
			throw this.createError(new LiteralText("Not enough arguments!"));
		}

		return this.args.get(++this.index).getValue();
	}

	/**
	 * Try to read the next argument, advancing the current index if successful
	 * or returning an absent optional if not.
	 *
	 * @return The optional next argument.
	 */
	public Optional<String> nextIfPresent() {
		return this.hasNext() ? Optional.of(this.args.get(++this.index).getValue()) : Optional.empty();
	}

	/**
	 * Create a parse exception with the provided message which has the position
	 * of the last parsed argument attached. The returned exception must be
	 * thrown at the target
	 *
	 * @param message The message for the exception
	 * @return the newly created, but unthrown exception
	 */
	public ArgumentParseException createError(Text message) {
		return new ArgumentParseException(message, this.rawInput, this.index < 0 ? 0 : this.args.get(this.index).getStartIdx());
	}

	/**
	 * Gets a list of all arguments as a string. The returned list is immutable.
	 *
	 * @return all arguments
	 */
	public List<String> getAll() {
		return Collections.unmodifiableList(this.args.stream().map(SingleArg::getValue).collect(Collectors.toList()));
	}

	List<SingleArg> getArgs() {
		return this.args;
	}

	/**
	 * Return this arguments object's current state. Can be used to reset with
	 * the {@link #setState(Object)} method.
	 *
	 * @return The current state
	 * @deprecated Use {@link #getSnapshot()} and
	 * {@link #applySnapshot(Snapshot)} instead
	 */
	@Deprecated
	public Object getState() {
		return this.getSnapshot();
	}

	/**
	 * Restore the arguments object's state to a state previously used.
	 *
	 * @param state the previous state
	 * @deprecated Use {@link #getSnapshot()} and
	 * {@link #applySnapshot(Snapshot)} instead
	 */
	@Deprecated
	public void setState(Object state) {
		if (!(state instanceof Snapshot)) {
			throw new IllegalArgumentException("Provided state was not of appropriate format returned by getState!");
		}

		this.applySnapshot((Snapshot) state, false); // keep parity with before
	}

	/**
	 * Return the raw string used to provide input to this arguments object.
	 *
	 * @return The raw input
	 */
	public String getRaw() {
		return this.rawInput;
	}

	/**
	 * Get an arg at the specified position.
	 *
	 * @param index index of the element to return
	 */
	public String get(int index) {
		return this.args.get(index).getValue();
	}

	/**
	 * Insert an arg as the next arg to be returned by {@link #next()}.
	 *
	 * @param value The argument to insert
	 */
	public void insertArg(String value) {
		int index = this.index < 0 ? 0 : this.args.get(this.index).getEndIdx();
		this.args.add(this.index + 1, new SingleArg(value, index, index));
	}

	/**
	 * Remove the arguments parsed between startState and endState.
	 *
	 * @param startState The starting state
	 * @param endState   The ending state
	 * @deprecated Use with {@link #getSnapshot()} instead of
	 * {@link #getState()} with {@link #removeArgs(Snapshot, Snapshot)}
	 */
	@Deprecated
	public void removeArgs(Object startState, Object endState) {
		if (!(startState instanceof Integer) || !(endState instanceof Integer)) {
			throw new IllegalArgumentException("One of the states provided was not of the correct type!");
		}

		this.removeArgs((int) startState, (int) endState);
	}

	/**
	 * Remove the arguments parsed between two snapshots.
	 *
	 * @param startSnapshot The starting state
	 * @param endSnapshot   The ending state
	 */
	public void removeArgs(Snapshot startSnapshot, Snapshot endSnapshot) {
		this.removeArgs(startSnapshot.index, endSnapshot.index);
	}

	private void removeArgs(int startIdx, int endIdx) {
		if (this.index >= startIdx) {
			if (this.index < endIdx) {
				this.index = startIdx - 1;
			} else {
				this.index -= (endIdx - startIdx) + 1;
			}
		}

		for (int i = startIdx; i <= endIdx; ++i) {
			this.args.remove(startIdx);
		}
	}

	/**
	 * Returns the number of arguments.
	 *
	 * @return the number of arguments
	 */
	public int size() {
		return this.args.size();
	}

	/**
	 * Go back to the previous argument.
	 */
	void previous() {
		if (this.index > -1) {
			--this.index;
		}
	}

	/**
	 * Gets the current position in raw input.
	 *
	 * @return the raw position
	 */
	public int getRawPosition() {
		return this.index < 0 ? 0 : this.args.get(this.index).getStartIdx();
	}

	/**
	 * Gets a snapshot of the data inside this context to allow it to be
	 * restored later.
	 *
	 * @return The {@link Snapshot} containing the current state of the
	 * {@link CommandArgs}
	 */
	public Snapshot getSnapshot() {
		return new Snapshot(this.index, this.args);
	}

	/**
	 * Resets a {@link CommandArgs} to a previous state using a previously
	 * created {@link Snapshot}.
	 *
	 * @param snapshot The {@link Snapshot} to restore this context
	 *                 with
	 */
	public void applySnapshot(Snapshot snapshot) {
		this.applySnapshot(snapshot, true);
	}

	/**
	 * Resets a {@link CommandArgs} to a previous state using a previously
	 * created {@link Snapshot}.
	 *
	 * <p>If resetArgs is set to false, this snapshot will not reset the
	 * argument list to its previous state, only the index.</p>
	 *
	 * @param snapshot  The {@link Snapshot} to restore this context
	 *                  with
	 * @param resetArgs Whether to restore the argument list
	 */
	public void applySnapshot(Snapshot snapshot, boolean resetArgs) {
		this.index = snapshot.index;

		if (resetArgs) {
			this.args.clear();
			this.args.addAll(snapshot.args);
		}
	}

	/**
	 * A snapshot of a {@link CommandArgs}. This object does not contain any
	 * public API methods, a snapshot should be considered a black box.
	 */
	public final class Snapshot {
		final int index;
		final ImmutableList<SingleArg> args;

		Snapshot(int index, List<SingleArg> args) {
			this.index = index;
			this.args = ImmutableList.copyOf(args);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}

			Snapshot snapshot = (Snapshot) o;
			return this.index == snapshot.index
					&& Objects.equals(this.args, snapshot.args);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.index, this.args);
		}
	}
}
