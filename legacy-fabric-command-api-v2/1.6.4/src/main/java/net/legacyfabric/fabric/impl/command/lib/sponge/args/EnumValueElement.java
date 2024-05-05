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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.text.ChatMessage;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.PatternMatchingCommandElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class EnumValueElement<T extends Enum<T>> extends PatternMatchingCommandElement {
	private final Class<T> type;
	private final Map<String, T> values;

	public EnumValueElement(ChatMessage key, Class<T> type) {
		super(key);
		this.type = type;
		this.values = Arrays.stream(type.getEnumConstants())
				.collect(Collectors.toMap(value -> value.name().toLowerCase(),
						Function.identity(), (value, value2) -> {
							throw new UnsupportedOperationException(type.getCanonicalName() + " contains more than one enum constant " + "with the same name, only differing by capitalization, which is unsupported.");
						}
				));
	}

	@Override
	protected Iterable<String> getChoices(PermissibleCommandSource source) {
		return this.values.keySet();
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		T value = this.values.get(choice.toLowerCase());

		if (value == null) {
			throw new IllegalArgumentException("No enum constant " + this.type.getCanonicalName() + "." + choice);
		}

		return value;
	}
}
