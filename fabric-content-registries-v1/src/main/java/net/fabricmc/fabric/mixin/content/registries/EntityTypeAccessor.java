/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
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

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDispatcher;

/**
 * Helper class that provides accessors for entity maps in {@link EntityDispatcher}.
 */
@Mixin(EntityDispatcher.class)
public interface EntityTypeAccessor {
	@Accessor("NAME_CLASS_MAP")
	static Map<String, Class<? extends Entity>> getNameClassMap() {
		throw new AssertionError();
	}

	@Accessor("CLASS_NAME_MAP")
	static Map<Class<? extends Entity>, String> getClassNameMap() {
		throw new AssertionError();
	}

	@Accessor("ID_CLASS_MAP")
	static Map<Integer, Class<? extends Entity>> getIdClassMap() {
		throw new AssertionError();
	}

	@Accessor("CLASS_ID_MAP")
	static Map<Class<? extends Entity>, Integer> getClassIdMap() {
		throw new AssertionError();
	}

	@Accessor("NAME_ID_MAP")
	static Map<String, Integer> getNameIdMap() {
		throw new AssertionError();
	}

	/**
	 * For internal use only!
	 */
	@Accessor("ID_CLASS_MAP")
	static void setIdClassMap(Map<Integer, Class<? extends Entity>> map) {
		throw new AssertionError();
	}

	/**
	 * For internal use only!
	 */
	@Accessor("CLASS_ID_MAP")
	static void setClassIdMap(Map<Class<? extends Entity>, Integer> map) {
		throw new AssertionError();
	}

	/**
	 * For internal use only!
	 */
	@Accessor("NAME_ID_MAP")
	static void setNameIdMap(Map<String, Integer> map) {
		throw new AssertionError();
	}

	@Invoker
	static void invokeRegisterEntity(Class<? extends Entity> clazz, String name, int id) {
		throw new AssertionError();
	}
}
