package net.fabricmc.fabric.mixin.content.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_635;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;

import net.fabricmc.fabric.api.content.registries.v1.BlockRegistry;
import net.fabricmc.fabric.api.content.registries.v1.ItemRegistry;
import net.fabricmc.fabric.impl.content.registries.ContentRegistryImpl;

@Mixin(World.class)
public class WorldMixin {
	@Shadow
	@Final
	public boolean isClient;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void init(class_635 arg, LevelProperties levelProperties, Dimension dimension, Profiler profiler, boolean client, CallbackInfo ci) {
		if (!isClient) {
			try {
				if (!BlockRegistry.blockIdsSetup) {
					File blockIds = new File(arg.getDataFile("blocks").getAbsoluteFile().getAbsolutePath().replace(".dat", ".registry"));
					blockIds.getParentFile().mkdirs();
					BiMap<Integer, Identifier> idMap = HashBiMap.create();

					if (blockIds.exists()) {
						FileReader fileReader = new FileReader(blockIds);
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

					if (itemIds.exists()) {
						FileReader fileReader = new FileReader(itemIds);
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

					ContentRegistryImpl.fillItemsMapWithUnknownEntries(idMap);
					PrintWriter writer = new PrintWriter(new FileOutputStream(itemIds, false));

					for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
						writer.write(entry.getKey() + "\t" + entry.getValue().toString() + "\n");
					}

					writer.close();
					ContentRegistryImpl.reorderItemEntries(idMap);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
