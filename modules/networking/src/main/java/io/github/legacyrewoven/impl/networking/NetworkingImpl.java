/*
 * Copyright (c) 2021 Legacy Rewoven
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

package io.github.legacyrewoven.impl.networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NetworkingImpl {
	public static final String MOD_ID = "fabric-networking-api-v1";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	/**
	 * Id of packet used to register supported channels.
	 */
	public static final String REGISTER_CHANNEL = "REGISTER";
	/**
	 * Id of packet used to unregister supported channels.
	 */
	public static final String UNREGISTER_CHANNEL = "UNREGISTER";
	/**
	 * Id of the packet used to declare all currently supported channels.
	 * Dynamic registration of supported channels is still allowed using {@link NetworkingImpl#REGISTER_CHANNEL} and {@link NetworkingImpl#UNREGISTER_CHANNEL}.
	 */
	public static final String EARLY_REGISTRATION_CHANNEL = MOD_ID + ":early_registration";

	public static boolean isReservedPlayChannel(String channelName) {
		return channelName.equals(REGISTER_CHANNEL) || channelName.equals(UNREGISTER_CHANNEL);
	}
}
