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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

import net.fabricmc.fabric.api.block.FabricBlockMaterial;
import net.fabricmc.fabric.impl.content.registries.FabricAxeItem;
import net.fabricmc.fabric.impl.content.registries.FabricPickaxeItem;
import net.fabricmc.fabric.impl.content.registries.FabricShovelItem;

@Mixin(Block.class)
public class MixinBlock {
	@Inject(method = "<init>(Lnet/minecraft/block/Material;Lnet/minecraft/block/MaterialColor;)V", at = @At("RETURN"))
	public void injectMaterial(Material material, MaterialColor color, CallbackInfo info) {
		if (!(FabricPickaxeItem.BLOCKS_BY_MINING_LEVEL.containsKey((Block) (Object) this)) && (material == Material.STONE || material == Material.IRON || material == Material.IRON2)) {
			if (material instanceof FabricBlockMaterial) {
				FabricPickaxeItem.MAP_ADDER.accept((Block) (Object) this, ((FabricBlockMaterial) material).getMiningLevel());
			} else {
				FabricPickaxeItem.MAP_ADDER.accept((Block) (Object) this, 0);
			}
		} else if (!(FabricShovelItem.BLOCKS_BY_MINING_LEVEL.containsKey((Block) (Object) this)) && (material == Material.GRASS || material == Material.DIRT || material == Material.CLAY || material == Material.SNOW || material == Material.SNOW_LAYER || material == Material.NOTEBLOCK)) {
			if (material instanceof FabricBlockMaterial) {
				FabricShovelItem.MAP_ADDER.accept((Block) (Object) this, ((FabricBlockMaterial) material).getMiningLevel());
			} else {
				FabricShovelItem.MAP_ADDER.accept((Block) (Object) this, 0);
			}
		} else if (!(FabricAxeItem.BLOCKS_BY_MINING_LEVEL.containsKey((Block) (Object) this)) && (material == Material.WOOD || material == Material.FOILAGE)) {
			if (material instanceof FabricBlockMaterial) {
				FabricAxeItem.MAP_ADDER.accept((Block) (Object) this, ((FabricBlockMaterial) material).getMiningLevel());
			} else {
				FabricAxeItem.MAP_ADDER.accept((Block) (Object) this, 0);
			}
		}
	}
}
