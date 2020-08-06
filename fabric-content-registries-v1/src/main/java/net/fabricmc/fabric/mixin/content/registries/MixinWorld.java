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

package net.fabricmc.fabric.mixin.content.registries;

import java.io.*;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;

import net.fabricmc.fabric.api.content.registry.v1.BlockRegistry;
import net.fabricmc.fabric.api.content.registry.v1.EntityRegistry;
import net.fabricmc.fabric.api.content.registry.v1.ItemRegistry;
import net.fabricmc.fabric.impl.content.registries.ContentRegistryImpl;

@Mixin(World.class)
public class MixinWorld {
	@Shadow
	@Final
	public boolean isClient;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void init(SaveHandler arg, LevelProperties levelProperties, Dimension dimension, Profiler profiler, boolean client, CallbackInfo ci) {
		if (!isClient) {
			try {
				if (!BlockRegistry.blockIdsSetup) {
					File blockIds = new File(arg.getDataFile("blocks").getAbsoluteFile().getAbsolutePath().replace(".dat", ".registry"));
					blockIds.getParentFile().mkdirs();
					BiMap<Integer, Identifier> idMap = HashBiMap.create();
					this.readIdsFromFile(idMap, blockIds);

					ContentRegistryImpl.fillBlocksMapWithUnknownEntries(idMap);
					PrintWriter writer = new PrintWriter(new FileOutputStream(blockIds, false));

					for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
						writer.write(entry.getKey() + "\t" + entry.getValue().toString() + "\n");
					}

					writer.close();
					ContentRegistryImpl.reorderBlockEntries(idMap);
				}

				if (!ItemRegistry.itemIdsSetup) {
					File itemIds = new File(arg.getDataFile("items").getAbsoluteFile().getAbsolutePath().replace(".dat", ".registry"));
					itemIds.getParentFile().mkdirs();
					BiMap<Integer, Identifier> idMap = HashBiMap.create();
					this.readIdsFromFile(idMap, itemIds);

					ContentRegistryImpl.fillItemsMapWithUnknownEntries(idMap);
					PrintWriter writer = new PrintWriter(new FileOutputStream(itemIds, false));

					for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
						writer.write(entry.getKey() + "\t" + entry.getValue().toString() + "\n");
					}

					writer.close();
					ContentRegistryImpl.reorderItemEntries(idMap);
				}

				if (!EntityRegistry.entityIdsSetup) {
					File entityIds = new File(arg.getDataFile("entities").getAbsoluteFile().getAbsolutePath().replace(".dat", ".registry"));
					entityIds.getParentFile().mkdirs();
					BiMap<Integer, String> idMap = HashBiMap.create();
					this.readIdsFromFile(entityIds, idMap);

					ContentRegistryImpl.fillEntitiesMapWithUnknownEntries(idMap);
					PrintWriter writer = new PrintWriter(new FileOutputStream(entityIds, false));

					for (Map.Entry<Integer, String> entry : idMap.entrySet()) {
						writer.write(entry.getKey() + "\t" + entry.getValue().toString() + "\n");
					}

					writer.close();
					ContentRegistryImpl.reorderEntityEntries(idMap);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Unique
	private void readIdsFromFile(BiMap<Integer, Identifier> idMap, File file) throws IOException {
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				if (line.isEmpty()) continue;
				int index = Integer.parseInt(line.substring(0, line.indexOf('\t')));
				String id = line.substring(line.indexOf('\t') + 1);
				idMap.put(index, new Identifier(id));
			}

			fileReader.close();
		}
	}

	@Unique
	private void readIdsFromFile(File file, BiMap<Integer, String> idMap) throws IOException {
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				if (line.isEmpty()) continue;
				int index = Integer.parseInt(line.substring(0, line.indexOf('\t')));
				String id = line.substring(line.indexOf('\t') + 1);
				idMap.put(index, id);
			}

			fileReader.close();
		}
	}
}
