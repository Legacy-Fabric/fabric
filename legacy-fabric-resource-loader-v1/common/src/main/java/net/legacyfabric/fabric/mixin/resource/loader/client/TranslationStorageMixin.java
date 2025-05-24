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

package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import net.legacyfabric.fabric.api.util.VersionUtils;
import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;

@Mixin(TranslationStorage.class)
public abstract class TranslationStorageMixin {
	@Shadow
	protected abstract void load(List<Resource> resources) throws IOException;

	@Shadow
	@Final
	private static Splitter TOKEN_SPLITTER;
	@Shadow
	@Final
	private static Pattern TOKEN_PATTERN;
	@Shadow
	Map<String, String> translations;

	@Unique
	private static final boolean isUpperCase = VersionUtils.matches("<1.11");

	@Unique
	private static final JsonParser JSON_PARSER = new JsonParser();

	@Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/TranslationStorage;setRightToLeft()V"))
	private void loadLangFileFromOtherVersion(ResourceManager resourceManager, List<String> languages, CallbackInfo ci) {
		for (String string : languages) {
			String langName = String.format("lang/%s.lang", getCorrectLangCode(string, false));
			String jsonName = String.format("lang/%s.json", getCorrectLangCode(string, true));

			for (String namespace : resourceManager.getAllNamespaces()) {
				boolean isStyleHappy = false;

				try {
					this.load(resourceManager.getAllResources(new Identifier(namespace, langName)));
				} catch (IOException ignored) {
					isStyleHappy = true;
				}

				try {
					this.load(resourceManager.getAllResources(new Identifier(namespace, jsonName)));
				} catch (IOException ignored) {
					isStyleHappy = true;
				}
			}
		}
	}

	private String getCorrectLangCode(String original, boolean json) {
		if (json || !isUpperCase) {
			return original.toLowerCase(Locale.ENGLISH);
		} else {
			String[] parts = original.split("_");

			if (parts.length > 1) {
				return parts[0] + "_" + parts[1].toUpperCase(Locale.ENGLISH);
			}
		}

		return original;
	}

	/**
	 * @author CatCore
	 * @reason optimize + load json files correctly
	 */
	@Overwrite
	private void load(InputStream stream) throws IOException {
		List<String> lines = IOUtils.readLines(stream, Charsets.UTF_8);

		if (lines.get(0).startsWith("{")) { // Load as json
			String content = String.join("\n", lines);
			JsonObject object = JSON_PARSER.parse(content).getAsJsonObject();
			recursiveLoadTranslations("", object);
		} else { // Load as properties/lang
			for (String string : lines) {
				if (!string.isEmpty() && string.charAt(0) != '#') {
					String[] strings = Iterables.toArray(TOKEN_SPLITTER.split(string), String.class);

					if (strings != null && strings.length == 2) {
						String string2 = strings[0];
						String string3 = TOKEN_PATTERN.matcher(strings[1]).replaceAll("%$1s");
						this.translations.put(string2, string3);
					}
				}
			}
		}
	}

	private void recursiveLoadTranslations(@NotNull String currentKey, JsonObject obj) {
		for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
			if (entry.getValue() instanceof JsonObject) {
				recursiveLoadTranslations((currentKey.isEmpty() ? "" : currentKey + ".") + entry.getKey(), (JsonObject) entry.getValue());
			} else {
				String key = currentKey;

				if (!key.isEmpty()) {
					if (!entry.getKey().equals("value")) {
						key = key + "." + entry.getKey();
					}
				} else {
					key = entry.getKey();
				}

				JsonElement value = entry.getValue();

				if (value.isJsonPrimitive()) {
					this.translations.put(key, TOKEN_PATTERN.matcher(value.getAsString()).replaceAll("%$1s"));
				} else {
					ResourceManagerHelperImpl.LOGGER.warn("Skipping translation key \"" + key + "\" with unsupported format " + value);
				}
			}
		}
	}
}
