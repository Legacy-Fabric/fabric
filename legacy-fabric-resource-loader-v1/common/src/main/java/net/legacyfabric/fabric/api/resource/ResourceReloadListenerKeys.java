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

package net.legacyfabric.fabric.api.resource;

import net.legacyfabric.fabric.api.util.Identifier;

/**
 * This class contains default keys for various Minecraft resource reload listeners.
 *
 * @see IdentifiableResourceReloadListener
 */
public final class ResourceReloadListenerKeys {
	// client
	public static final Identifier SOUNDS = new Identifier("minecraft:sounds");
	public static final Identifier FONTS = new Identifier("minecraft:fonts");
	public static final Identifier MODELS = new Identifier("minecraft:models");
	public static final Identifier LANGUAGES = new Identifier("minecraft:languages");
	public static final Identifier TEXTURES = new Identifier("minecraft:textures");

	private ResourceReloadListenerKeys() {
	}
}
