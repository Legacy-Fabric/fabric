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

package net.fabricmc.fabric.api.network;

import javax.annotation.Nullable;

public class PacketIdentifier {
	private final String namespace;
	private final String channel;

	public PacketIdentifier(String namespace, String channel) {
		this.namespace = namespace;
		this.channel = channel;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getChannel() {
		return this.channel;
	}

	@Override
	public String toString() {
		return this.namespace + "|" + this.channel;
	}

	@Nullable
	public static PacketIdentifier parse(String identifier) {
		if (identifier.contains("|")) {
			String[] arr = identifier.split("\\|");
			return new PacketIdentifier(arr[0], arr[1]);
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PacketIdentifier)) {
			return false;
		}

		return obj.toString().equals(this.toString()) || super.equals(obj);
	}
}
