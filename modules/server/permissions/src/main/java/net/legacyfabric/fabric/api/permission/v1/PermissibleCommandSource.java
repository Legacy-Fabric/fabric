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

package net.legacyfabric.fabric.api.permission.v1;

import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a {@link CommandSource} that can not be able to run
 * a command if they do not have the permission to do so.
 *
 * @deprecated Unstable API, may change in the future.
 */
@Deprecated
@ApiStatus.Experimental
public interface PermissibleCommandSource extends CommandSource {
	boolean hasPermission(String perm);
}
