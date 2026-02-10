/*
 * Copyright (c) 2020 - 2026 Legacy Fabric
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

package net.legacyfabric.fabric.api.util;

import java.util.Locale;

public class Identifier implements Comparable<Identifier> {
	protected final String namespace;
	protected final String path;

	private Identifier(boolean keepCase, String... pathParts) {
		this.namespace = pathParts[0] == null || pathParts[0].isEmpty() ? "minecraft" : pathParts[0].toLowerCase(Locale.ENGLISH);
		this.path = keepCase ? pathParts[1] : pathParts[1].toLowerCase(Locale.ENGLISH);

		if (this.path == null) {
			throw new IllegalArgumentException("Path cannot be null!");
		}
	}

	public Identifier(String name) {
		this(false, parseString(name));
	}

	public Identifier(Object object) {
		this(false, parseString(object.toString()));
	}

	public Identifier(String namespace, String path) {
		this(false, namespace, path);
	}

	public Identifier(String namespace, String path, boolean keepCase) {
		this(keepCase, namespace, path);
	}

	protected static String[] parseString(String path) {
		String[] strings = new String[]{"minecraft", path};
		int i = path.indexOf(58);

		if (i >= 0) {
			strings[1] = path.substring(i + 1);

			if (i > 1) {
				strings[0] = path.substring(0, i);
			}
		}

		return strings;
	}

	public String getPath() {
		return this.path;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String toString() {
		return this.namespace + ':' + this.path;
	}

	public String toTranslationKey() {
		return this.namespace + "." + this.path;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof Identifier)) {
			return false;
		} else {
			Identifier identifier = (Identifier) object;
			return this.namespace.equals(identifier.namespace) && this.path.equals(identifier.path);
		}
	}

	public int hashCode() {
		return 31 * this.namespace.hashCode() + this.path.hashCode();
	}

	public int compareTo(Identifier identifier) {
		int i = this.namespace.compareTo(identifier.namespace);

		if (i == 0) {
			i = this.path.compareTo(identifier.path);
		}

		return i;
	}
}
