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

package net.legacyfabric.fabric.mixin.resource.loader.client.osl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.ornithemc.osl.localization.impl.Locale;
import org.quiltmc.parsers.json.JsonReader;
import org.quiltmc.parsers.json.JsonToken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;

@Mixin(value = Locale.class, remap = false)
public abstract class LocaleMixin {
	@Shadow(remap = false)
	protected abstract void set(String key, String translation);

	@Inject(method = "loadFromJson", at = @At("HEAD"), cancellable = true, remap = false)
	private void lf$readJsonRecursively(InputStream is, CallbackInfo ci) throws IOException {
		try (JsonReader reader = JsonReader.json(new InputStreamReader(is))) {
			reader.beginObject();
			lf$recursiveLoadTranslations("", reader);
			reader.endObject();
		}

		ci.cancel();
	}

	@Unique
	private void lf$recursiveLoadTranslations(String currentKey, JsonReader reader) throws IOException {
		while (reader.hasNext()) {
			String entryKey = reader.nextName();

			if (reader.peek() == JsonToken.BEGIN_OBJECT) {
				String prefix = currentKey.isEmpty() ? entryKey : currentKey + "." + entryKey;
				reader.beginObject();
				lf$recursiveLoadTranslations(prefix, reader);
				reader.endObject();
			} else {
				String key;

				if (!currentKey.isEmpty()) {
					key = entryKey.equals("value") ? currentKey : currentKey + "." + entryKey;
				} else {
					key = entryKey;
				}

				if (reader.peek() == JsonToken.STRING) {
					this.set(key, reader.nextString());
				} else {
					ResourceManagerHelperImpl.LOGGER.warn("Skipping translation key \"" + key + "\" with unsupported format");
					reader.skipValue();
				}
			}
		}
	}
}
