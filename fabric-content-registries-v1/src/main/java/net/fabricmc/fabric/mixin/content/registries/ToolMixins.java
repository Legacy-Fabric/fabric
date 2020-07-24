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

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;

import net.fabricmc.fabric.impl.content.registries.FabricAxeItem;
import net.fabricmc.fabric.impl.content.registries.FabricPickaxeItem;
import net.fabricmc.fabric.impl.content.registries.FabricShovelItem;

public class ToolMixins {
	@Mixin(PickaxeItem.class)
	public abstract static class MixinPickaxeItem extends ToolItem {
		protected MixinPickaxeItem(float attackDamage, ToolMaterialType material, Set<Block> effectiveBlocks) {
			super(attackDamage, material, effectiveBlocks);
		}

		@Inject(at = @At("HEAD"), method = "isEffectiveOn", cancellable = true)
		public void isEffectiveOn(Block block, CallbackInfoReturnable<Boolean> info) {
			if (FabricPickaxeItem.BLOCKS_BY_MINING_LEVEL.containsKey(block)) {
				info.setReturnValue(this.material.getMiningLevel() >= FabricPickaxeItem.BLOCKS_BY_MINING_LEVEL.get(block));
			}
		}
	}

	@Mixin(ShovelItem.class)
	public abstract static class MixinShovelItem extends ToolItem {
		protected MixinShovelItem(float attackDamage, ToolMaterialType material, Set<Block> effectiveBlocks) {
			super(attackDamage, material, effectiveBlocks);
		}

		@Inject(at = @At("HEAD"), method = "isEffectiveOn", cancellable = true)
		public void isEffectiveOn(Block block, CallbackInfoReturnable<Boolean> info) {
			if (FabricShovelItem.BLOCKS_BY_MINING_LEVEL.containsKey(block)) {
				info.setReturnValue(this.material.getMiningLevel() >= FabricShovelItem.BLOCKS_BY_MINING_LEVEL.get(block));
			}
		}
	}

	@Mixin(ShovelItem.class)
	public abstract static class MixinAxeItem extends ToolItem {
		protected MixinAxeItem(float attackDamage, ToolMaterialType material, Set<Block> effectiveBlocks) {
			super(attackDamage, material, effectiveBlocks);
		}

		@Inject(at = @At("HEAD"), method = "isEffectiveOn", cancellable = true)
		public void isEffectiveOn(Block block, CallbackInfoReturnable<Boolean> info) {
			if (FabricAxeItem.BLOCKS_BY_MINING_LEVEL.containsKey(block)) {
				info.setReturnValue(this.material.getMiningLevel() >= FabricAxeItem.BLOCKS_BY_MINING_LEVEL.get(block));
			}
		}
	}
}
