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

package net.legacyfabric.fabric.mixin.registry.sync.osl;

import java.io.File;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.storage.AlphaWorldStorage;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.legacyfabric.fabric.impl.logger.LoggerImpl;

@Mixin(value = AlphaWorldStorage.class, priority = 1500)
public class OSLAlphaWorldStorageMixin {
	@Unique
	private static final int FABRIC_ID_REGISTRY_BACKUPS = 3;
	@Unique
	private static final Logger LOGGER = Logger.get(LoggerImpl.API, "WorldSaveHandler");
	@Final
	@Shadow
	private File dir;

	@Unique
	private File fabric_getWorldIdMapFile(int i) {
		return new File(new File(this.dir, "data"), "fabricRegistry" + ".dat" + (i == 0 ? "" : ("." + i)));
	}

	@TargetHandler(
			mixin = "net.ornithemc.osl.registries.impl.mixin.common.AlphaWorldStorageMixin",
			name = "osl$registries$loadRegistryMappings"
	)
	@WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/ornithemc/osl/registries/impl/mixin/common/AlphaWorldStorageMixin;readRegistryMappings(Ljava/io/File;Z)Z", ordinal = 0))
	private boolean lf$readLegacyFabricRegistryMappings(@Coerce AlphaWorldStorage instance, File file, boolean throwOnException, Operation<Boolean> original) {
		for (int i = 0; i < FABRIC_ID_REGISTRY_BACKUPS; i++) {
			LOGGER.trace("[legacy-fabric-registry-sync-api-v1] Loading old Legacy Fabric registry mappings [file " + (i + 1) + "/" + (FABRIC_ID_REGISTRY_BACKUPS + 1) + "]");

			if (original.call(instance, fabric_getWorldIdMapFile(i), throwOnException)) {
				LOGGER.info("[legacy-fabric-registry-sync-api-v1] Loaded old Legacy Fabric registry data [file " + (i + 1) + "/" + (FABRIC_ID_REGISTRY_BACKUPS + 1) + "]");
				return true;
			}
		}

		return original.call(instance, file, throwOnException);
	}

	@TargetHandler(
			mixin = "net.ornithemc.osl.registries.impl.mixin.common.AlphaWorldStorageMixin",
			name = "writeRegistryMappings"
	)
	@Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
	private void lf$backupLegacyFabricRegistryMappings(File file, File newFile, File oldFile, CallbackInfo ci) {
		for (int i = FABRIC_ID_REGISTRY_BACKUPS - 1; i >= 0; i--) {
			File lf_file = fabric_getWorldIdMapFile(i);

			if (lf_file.exists()) {
				if (i != 0) {
					lf_file.delete();
				} else {
					lf_file.renameTo(file);

					if (lf_file.exists()) {
						lf_file.delete();
					}
				}
			}
		}
	}
}
