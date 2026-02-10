/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.locale.Language;

import net.legacyfabric.fabric.api.resource.ResourcePackManager;
import net.legacyfabric.fabric.api.util.Identifier;
import net.legacyfabric.fabric.impl.resource.loader.IOBiConsumer;
import net.legacyfabric.fabric.impl.resource.loader.ResourcePackManagerImpl;

@Mixin(Language.class)
public class LanguageMixin {
	@Unique
	private static JsonParser JSON_PARSER;

	@Inject(method = "loadTranslations", at = @At("RETURN"))
	private void loadModTranslations(Properties translations, String language, CallbackInfo ci) {
		for (String namespace : ResourcePackManager.getNamespaces()) {
			loadTranslationFiles(translations, new Identifier(namespace, "lang/" + language + ".lang", true), this::loadLangFile);
			loadTranslationFiles(translations, new Identifier(namespace, "lang/" + getLowercaseLangCode(language) + ".lang"), this::loadLangFile);
			loadTranslationFiles(translations, new Identifier(namespace, "lang/" + getLowercaseLangCode(language) + ".json"), this::loadJsonLangFile);
		}
	}

	private String getLowercaseLangCode(String original) {
		return original.toLowerCase(Locale.ENGLISH);
	}

	private void loadTranslationFiles(Properties translations, Identifier path, IOBiConsumer<Properties, InputStream> consumer) {
		for (InputStream stream : ResourcePackManager.openAllFiles(path)) {
			try {
				consumer.accept(translations, stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Void loadLangFile(Properties translations, InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();

			if (!line.startsWith("#")) {
				String[] parts = line.split("=");

				if (parts != null && parts.length == 2) {
					translations.setProperty(parts[0], parts[1]);
				}
			}
		}

		return null;
	}

	private Void loadJsonLangFile(Properties translations, InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		if (JSON_PARSER == null) JSON_PARSER = new JsonParser();
		JsonObject element = JSON_PARSER.parse(reader).getAsJsonObject();
		recursiveLoadTranslations(translations, "", element);
		return null;
	}

	private void recursiveLoadTranslations(Properties translations, @NotNull String currentKey, JsonObject obj) {
		for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
			if (entry.getValue() instanceof JsonObject) {
				recursiveLoadTranslations(translations, (currentKey.isEmpty() ? "" : currentKey + ".") + entry.getKey(), (JsonObject) entry.getValue());
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
					translations.setProperty(key, value.getAsString());
				} else {
					ResourcePackManagerImpl.LOGGER.warn("Skipping translation key \"" + key + "\" with unsupported format " + value);
				}
			}
		}
	}
}
