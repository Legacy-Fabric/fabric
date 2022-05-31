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

package net.legacyfabric.fabric.impl.command.lib.sponge.args;

import org.jetbrains.annotations.Nullable;

import net.minecraft.text.Text;

import net.legacyfabric.fabric.api.command.v2.StringType;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.ArgumentParseException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandArgs;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.KeyElement;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;

public class StringCommandElement extends KeyElement {
	private final StringType stringType;
	private final RemainingJoinedStringsCommandElement joinedElement;

	public StringCommandElement(Text key, StringType type) {
		super(key);
		this.stringType = type;
		this.joinedElement = type.isAll() ? new RemainingJoinedStringsCommandElement(key, false) : null;
	}

	@Nullable
	@Override
	public Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException {
		return this.stringType.isAll() ? (String) this.joinedElement.parseValue(source, args) : args.next();
	}
}
