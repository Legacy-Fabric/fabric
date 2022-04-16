package net.legacyfabric.fabric.mixin.registry.sync;

import net.legacyfabric.fabric.impl.registry.sync.RegistryRemapperAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelProperties;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {
	@Unique
	private CompoundTag fabric_lastSavedIdMap = null;
	@Unique
	private static final int FABRIC_ID_REGISTRY_BACKUPS = 3;
	@Unique
	private static final Logger LOGGER = LogManager.getLogger();
	@Final
	@Shadow
	private File worldDir;

	@Unique
	private boolean fabric_readIdMapFile(File file) throws IOException {
		if (file.exists()) {
			CompoundTag tag;

			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				tag = NbtIo.readCompressed(fileInputStream);
			}

			if (tag != null) {
				((RegistryRemapperAccess) MinecraftServer.getServer()).readAndRemap(tag);
				return true;
			}
		}

		return false;
	}

	@Unique
	private File fabric_getWorldIdMapFile(int i) {
		return new File(new File(this.worldDir, "data"), "fabricRegistry" + ".dat" + (i == 0 ? "" : ("." + i)));
	}

	@Unique
	private void fabric_saveRegistryData() {
		CompoundTag newIdMap = ((RegistryRemapperAccess) MinecraftServer.getServer()).toRegistryTag();

		if (!newIdMap.equals(this.fabric_lastSavedIdMap)) {
			for (int i = FABRIC_ID_REGISTRY_BACKUPS - 1; i >= 0; i--) {
				File file = fabric_getWorldIdMapFile(i);

				if (file.exists()) {
					if (i == FABRIC_ID_REGISTRY_BACKUPS - 1) {
						file.delete();
					} else {
						File target = fabric_getWorldIdMapFile(i + 1);
						file.renameTo(target);
					}
				}
			}

			try {
				File file = fabric_getWorldIdMapFile(0);
				File parentFile = file.getParentFile();

				if (!parentFile.exists()) {
					if (!parentFile.mkdirs()) {
						LOGGER.warn("[legacy-fabric-registry-sync-api-v1] Could not create directory " + parentFile + "!");
					}
				}

				FileOutputStream fileOutputStream = new FileOutputStream(file);
				NbtIo.writeCompressed(newIdMap, fileOutputStream);
				fileOutputStream.close();
			} catch (IOException e) {
				LOGGER.warn("[legacy-fabric-registry-sync-api-v1] Failed to save registry file!", e);
			}

			fabric_lastSavedIdMap = newIdMap;
		}
	}

	@Inject(method = "saveWorld(Lnet/minecraft/world/level/LevelProperties;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
	public void saveWorld(LevelProperties levelProperties, CompoundTag compoundTag, CallbackInfo info) {
		if (!worldDir.exists()) {
			return;
		}

		fabric_saveRegistryData();
	}

	@Inject(method = "getLevelProperties", at = @At("HEAD"))
	public void readWorldProperties(CallbackInfoReturnable<LevelProperties> callbackInfo) {
		// Load
		for (int i = 0; i < FABRIC_ID_REGISTRY_BACKUPS; i++) {
			LOGGER.trace("[legacy-fabric-registry-sync-api-v1] Loading Legacy Fabric registry [file " + (i + 1) + "/" + (FABRIC_ID_REGISTRY_BACKUPS + 1) + "]");

			try {
				if (fabric_readIdMapFile(fabric_getWorldIdMapFile(i))) {
					LOGGER.info("[legacy-fabric-registry-sync-api-v1] Loaded registry data [file " + (i + 1) + "/" + (FABRIC_ID_REGISTRY_BACKUPS + 1) + "]");
					return;
				}
			} catch (FileNotFoundException e) {
				// pass
			} catch (IOException e) {
				if (i >= FABRIC_ID_REGISTRY_BACKUPS - 1) {
					throw new RuntimeException(e);
				} else {
					LOGGER.warn("Reading registry file failed!", e);
				}
			} catch (RuntimeException e) {
				throw new RuntimeException("Remapping world failed!", e);
			}
		}

		// If not returned (not present), try saving the registry data
		fabric_saveRegistryData();
	}
}
