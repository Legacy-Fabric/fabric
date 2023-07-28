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

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.legacyfabric.fabric.impl.command.MinecraftClientAccessor;

/**
 * Represents a command argument element.
 */
public abstract class CommandElement {
	@Nullable
	private final Text key;

	protected CommandElement(@Nullable Text key) {
		this.key = key;
	}

	/**
	 * Return the key to be used for this object.
	 *
	 * @return the user-facing representation of the key
	 */
	@Nullable
	public Text getKey() {
		return this.key;
	}

	/**
	 * Return the plain key, to be used when looking up this command element in
	 * a {@link CommandContext}. If the key is a {@link TranslatableText}, this
	 * is the translation's id. Otherwise, this is the result of
	 * {@link Text#asString()} ()} ()}.
	 *
	 * @return the raw key
	 */
	@Nullable
	public String getUntranslatedKey() {
		return CommandContext.textToArgKey(this.key);
	}

	/**
	 * Attempt to extract a value for this element from the given arguments and
	 * put it in the given context. This method normally delegates to
	 * {@link #parseValue(PermissibleCommandSource, CommandArgs)} for getting the values.
	 * This method is expected to have no side-effects for the source, meaning
	 * that executing it will not change the state of the {@link PermissibleCommandSource}
	 * in any way.
	 *
	 * @param source  The source to parse for
	 * @param args    The args to extract from
	 * @param context The context to supply to
	 * @throws ArgumentParseException if unable to extract a value
	 */
	public void parse(PermissibleCommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
		Object val = this.parseValue(source, args);
		String key = this.getUntranslatedKey();

		if (key != null && val != null) {
			if (val instanceof Iterable<?>) {
				for (Object ent : ((Iterable<?>) val)) {
					context.putArg(key, ent);
				}
			} else {
				context.putArg(key, val);
			}
		}
	}

	/**
	 * Attempt to extract a value for this element from the given arguments.
	 * This method is expected to have no side-effects for the source, meaning
	 * that executing it will not change the state of the {@link PermissibleCommandSource}
	 * in any way.
	 *
	 * @param source The source to parse for
	 * @param args   the arguments
	 * @return The extracted value
	 * @throws ArgumentParseException if unable to extract a value
	 */
	@Nullable
	public abstract Object parseValue(PermissibleCommandSource source, CommandArgs args) throws ArgumentParseException;

	/**
	 * Fetch completions for command arguments.
	 *
	 * @param src     The source requesting tab completions
	 * @param args    The arguments currently provided
	 * @param context The context to store state in
	 * @return Any relevant completions
	 */
	public abstract List<String> complete(PermissibleCommandSource src, CommandArgs args, CommandContext context);

	/**
	 * Return a usage message for this specific argument.
	 *
	 * @param src The source requesting usage
	 * @return The formatted usage
	 */
	public Text getUsage(PermissibleCommandSource src) {
		return this.getKey() == null ? new LiteralText("") : new LiteralText("<" + this.getKey().computeValue() + ">");
	}

	public MinecraftServer getServer() {
		MinecraftServer minecraftServer = null;

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			minecraftServer = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
		} else {
			try {
				minecraftServer = ((MinecraftClientAccessor) net.minecraft.client.MinecraftClient.getInstance()).getMinecraftServer();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}

		return minecraftServer;
	}
}
