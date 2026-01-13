/*
 * Copyright (c) 2020 - 2025 Legacy Fabric
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

package net.legacyfabric.fabric.impl.command;

import java.util.Arrays;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

/**
 * Guava's ToStringHelper class which isn't located at the same place depending on guava versions.
 * This ensures it is available in all targeted versions.
 */
public class GuavaUtils {
	public static ToStringHelper toStringHelper(Object self) {
		return new ToStringHelper(self.getClass().getSimpleName());
	}

	public static ToStringHelper toStringHelper(Class<?> clazz) {
		return new ToStringHelper(clazz.getSimpleName());
	}

	public static ToStringHelper toStringHelper(String className) {
		return new ToStringHelper(className);
	}

	public static final class ToStringHelper {
		private final String className;
		private final ValueHolder holderHead;
		private ValueHolder holderTail;
		private boolean omitNullValues;

		private ToStringHelper(String className) {
			this.holderHead = new ValueHolder();
			this.holderTail = this.holderHead;
			this.omitNullValues = false;
			this.className = (String) Preconditions.checkNotNull(className);
		}

		public ToStringHelper omitNullValues() {
			this.omitNullValues = true;
			return this;
		}

		public ToStringHelper add(String name, @Nullable Object value) {
			return this.addHolder(name, value);
		}

		public ToStringHelper add(String name, boolean value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper add(String name, char value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper add(String name, double value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper add(String name, float value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper add(String name, int value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper add(String name, long value) {
			return this.addHolder(name, String.valueOf(value));
		}

		public ToStringHelper addValue(@Nullable Object value) {
			return this.addHolder(value);
		}

		public ToStringHelper addValue(boolean value) {
			return this.addHolder(String.valueOf(value));
		}

		public ToStringHelper addValue(char value) {
			return this.addHolder(String.valueOf(value));
		}

		public ToStringHelper addValue(double value) {
			return this.addHolder(String.valueOf(value));
		}

		public ToStringHelper addValue(float value) {
			return this.addHolder(String.valueOf(value));
		}

		public ToStringHelper addValue(int value) {
			return this.addHolder(String.valueOf(value));
		}

		public ToStringHelper addValue(long value) {
			return this.addHolder(String.valueOf(value));
		}

		public String toString() {
			boolean omitNullValuesSnapshot = this.omitNullValues;
			String nextSeparator = "";
			StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');

			for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
				Object value = valueHolder.value;

				if (!omitNullValuesSnapshot || value != null) {
					builder.append(nextSeparator);
					nextSeparator = ", ";

					if (valueHolder.name != null) {
						builder.append(valueHolder.name).append('=');
					}

					if (value != null && value.getClass().isArray()) {
						Object[] objectArray = new Object[]{value};
						String arrayString = Arrays.deepToString(objectArray);
						builder.append(arrayString, 1, arrayString.length() - 1);
					} else {
						builder.append(value);
					}
				}
			}

			return builder.append('}').toString();
		}

		private ValueHolder addHolder() {
			ValueHolder valueHolder = new ValueHolder();
			this.holderTail = this.holderTail.next = valueHolder;
			return valueHolder;
		}

		private ToStringHelper addHolder(@Nullable Object value) {
			ValueHolder valueHolder = this.addHolder();
			valueHolder.value = value;
			return this;
		}

		private ToStringHelper addHolder(String name, @Nullable Object value) {
			ValueHolder valueHolder = this.addHolder();
			valueHolder.value = value;
			valueHolder.name = (String) Preconditions.checkNotNull(name);
			return this;
		}

		private static final class ValueHolder {
			String name;
			Object value;
			ValueHolder next;

			private ValueHolder() {
			}
		}
	}
}
