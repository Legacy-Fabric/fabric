/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.util;

public enum TriState {
	TRUE(true) {
		@Override
		public TriState and(TriState other) {
			return other == TRUE || other == DEFAULT ? TRUE : FALSE;
		}

		@Override
		public TriState or(TriState other) {
			return TRUE;
		}

		@Override
		public TriState negate() {
			return FALSE;
		}
	},
	FALSE(false) {
		@Override
		public TriState and(TriState other) {
			return FALSE;
		}

		@Override
		public TriState or(TriState other) {
			return other == TRUE ? TRUE : FALSE;
		}

		@Override
		public TriState negate() {
			return TRUE;
		}
	},
	DEFAULT(false) {
		@Override
		public TriState and(TriState other) {
			return other;
		}

		@Override
		public TriState or(TriState other) {
			return other;
		}

		@Override
		public TriState negate() {
			return this;
		}
	};

	private final boolean val;

	TriState(boolean val) {
		this.val = val;
	}
	
	public static TriState of(boolean b) {
		return b ? TRUE : FALSE;
	}

	public abstract TriState and(TriState other);

	public abstract TriState or(TriState other);

	public abstract TriState negate();

	public boolean getVal() {
		return this.val;
	}

	@Deprecated
	public boolean get() {
		return this == TRUE;
	}
}
