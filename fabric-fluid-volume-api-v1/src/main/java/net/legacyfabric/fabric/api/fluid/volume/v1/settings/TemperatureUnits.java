/*
 * Copyright (c) 2020 Legacy Fabric
 * Copyright (c) 2016 - 2020 FabricMC
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

package net.legacyfabric.fabric.api.fluid.volume.v1.settings;

/**
 * Helper class that stores temperature units.
 */
public interface TemperatureUnits {
	int ROOM_TEMPERATURE = 20;

	// Metals' melting points
	int COPPER = 1085;
	int ALUMINIUM = 660;
	int LEAD = 378;
	int IRON = 1204;
	int TIN = 232;
	int ZINC = 420;
	int NICKEL = 1453;
	int SILVER = 962;
	int PLATINUM = 1768;
	int GOLD = 1064;
}
