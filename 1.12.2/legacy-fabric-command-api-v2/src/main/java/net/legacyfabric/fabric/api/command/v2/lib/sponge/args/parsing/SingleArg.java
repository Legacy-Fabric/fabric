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

package net.legacyfabric.fabric.api.command.v2.lib.sponge.args.parsing;

import com.google.common.base.Objects;

/**
 * This represents a single argument with its start and end indexes
 * in the associated raw input string.
 */
public class SingleArg {
	private final String value;
	private final int startIdx;
	private final int endIdx;

	/**
	 * Create a new argument.
	 *
	 * @param value    The argument string
	 * @param startIdx The starting index of {@code value} in an input string
	 * @param endIdx   The ending index of {@code value} in an input string
	 */
	public SingleArg(String value, int startIdx, int endIdx) {
		this.value = value;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
	}

	/**
	 * Gets the string used.
	 *
	 * @return The string used
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Gets the starting index.
	 *
	 * @return The starting index
	 */
	public int getStartIdx() {
		return this.startIdx;
	}

	/**
	 * Gets the ending index.
	 *
	 * @return The ending index
	 */
	public int getEndIdx() {
		return this.endIdx;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof SingleArg)) {
			return false;
		}

		SingleArg singleArg = (SingleArg) o;
		return this.startIdx == singleArg.startIdx
				&& this.endIdx == singleArg.endIdx
				&& Objects.equal(this.value, singleArg.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.value, this.startIdx, this.endIdx);
	}

	//	@Override
	//	public String toString() {
	//		return Objects.toStringHelper(this)
	//				.add("value", this.value)
	//				.add("startIdx", this.startIdx)
	//				.add("endIdx", this.endIdx)
	//				.toString();
	//	}
}
