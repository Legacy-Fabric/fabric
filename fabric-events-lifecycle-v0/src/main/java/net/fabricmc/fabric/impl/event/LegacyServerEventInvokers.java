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

package net.fabricmc.fabric.impl.event;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.server.DedicatedServerSetupCallback;
import net.fabricmc.fabric.api.server.event.lifecycle.v1.DedicatedServerLifecycleEvents;

@Environment(EnvType.SERVER)
public class LegacyServerEventInvokers implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		DedicatedServerLifecycleEvents.POST_SETUP.register(server -> DedicatedServerSetupCallback.EVENT.invoker().onServerSetup(server));
	}
}
