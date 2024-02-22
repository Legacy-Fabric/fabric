/*
 * Copyright (c) 2020 - 2024 Legacy Fabric
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

package net.legacyfabric.fabric.mixin.registry.sync;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {
	@Mutable
	@Accessor
	static Map<String, Class<? extends Entity>> getNAME_CLASS_MAP() {
		return null;
	}

	@Mutable
	@Accessor
	static void setNAME_CLASS_MAP(Map<String, Class<? extends Entity>> map) {
	}

	@Mutable
	@Accessor
	static void setCLASS_NAME_MAP(Map<Class<? extends Entity>, String> map) {
	}

	@Mutable
	@Accessor
	static Map<Integer, Class<? extends Entity>> getID_CLASS_MAP() {
		return null;
	}

	@Mutable
	@Accessor
	static void setID_CLASS_MAP(Map<Integer, Class<? extends Entity>> map) {
	}

	@Mutable
	@Accessor
	static void setCLASS_ID_MAP(Map<Class<? extends Entity>, Integer> map) {
	}

	@Mutable
	@Accessor("field_3272")
	static Map<String, Integer> getNAME_ID_MAP() {
		return null;
	}

	@Mutable
	@Accessor("field_3272")
	static void setNAME_ID_MAP(Map<String, Integer> map) {
	}
}
