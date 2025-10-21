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

package net.legacyfabric.fabric.impl.resource.loader.client;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.texture.AbstractTexturePack;
import net.minecraft.client.texture.ITexturePack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.resource.ModResourcePack;
import net.legacyfabric.fabric.api.resource.ModTexturePack;

@Environment(EnvType.CLIENT)
public class ModNioTexturePack extends AbstractTexturePack implements ModTexturePack {
	private final ModResourcePack resourcePack;

	public ModNioTexturePack(ModResourcePack resourcePack) {
		super(resourcePack.getFabricModMetadata().getId(), null, resourcePack.getName());
		this.resourcePack = resourcePack;
		this.readManifest();
		this.readIcon();
	}

	@Override
	public InputStream openStream(String string) {
		if (this.resourcePack == null) {
			if ("/pack.png".equals(string) || "/pack.txt".equals(string)) {
				return ITexturePack.class.getResourceAsStream(string);
			}

			return null;
		}

		try {
			return this.resourcePack.openFile(string);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public ModResourcePack getResourcePack() {
		return this.resourcePack;
	}

	private void readIcon() {
		InputStream var1 = null;

		try {
			var1 = this.openStream("/pack.png");

			if (var1 == null) {
				return;
			}

			this.icon = ImageIO.read(var1);
		} catch (IOException var11) {
			return;
		} finally {
			try {
				if (var1 != null) {
					var1.close();
				}
			} catch (IOException var10) {
				return;
			}
		}
	}
}
