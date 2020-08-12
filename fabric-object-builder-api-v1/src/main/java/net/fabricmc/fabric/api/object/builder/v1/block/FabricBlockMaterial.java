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

package net.fabricmc.fabric.api.object.builder.v1.block;

import com.google.common.annotations.Beta;

import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

import net.fabricmc.fabric.mixin.object.builder.MaterialAccessor;

/**
 * Hooks for creating custom block Materials. Provides implementations for invisible methods and hooks not found in the original class.
 */
public class FabricBlockMaterial extends Material {
	private boolean fluid = false;
	private boolean collision = true;
	private boolean translucent = false;
	private boolean blocksMovement = true;

	public FabricBlockMaterial(MaterialColor color) {
		super(color);
	}

	@Override
	public FabricBlockMaterial requiresTool() {
		super.requiresTool();
		return this;
	}

	@Override
	public FabricBlockMaterial setFlammable() {
		super.setFlammable();
		return this;
	}

	@Override
	public FabricBlockMaterial setNoPushing() {
		super.setNoPushing();
		return this;
	}

	@Override
	public FabricBlockMaterial setImmovable() {
		super.setImmovable();
		return this;
	}

	@Override
	public FabricBlockMaterial setReplaceable() {
		super.setReplaceable();
		return this;
	}

	public FabricBlockMaterial setAsFluid() {
		this.fluid = true;
		return this;
	}

	public FabricBlockMaterial noCollision() {
		this.collision = false;
		return this;
	}

	public FabricBlockMaterial setTranslucent() {
		this.translucent = true;
		return this;
	}

	public FabricBlockMaterial doesNotBlockMovement() {
		this.blocksMovement = false;
		return this;
	}

	@Override
	public boolean method_1803() {
		return this.fluid;
	}

	@Override
	public boolean hasCollision() {
		return this.collision;
	}

	@Override
	public boolean isTransluscent() {
		return this.translucent;
	}

	@Override
	public boolean blocksMovement() {
		return this.blocksMovement;
	}

	@Override
	public FabricBlockMaterial setCanBeBrokenInAdventureMode() {
		super.setCanBeBrokenInAdventureMode();
		return this;
	}

	/**
	 * Subject to changes.
	 */
	@Beta
	public FabricBlockMaterial requiresSilkTouch() {
		return (FabricBlockMaterial) ((MaterialAccessor) this).invokeRequiresSilkTouch();
	}
}
