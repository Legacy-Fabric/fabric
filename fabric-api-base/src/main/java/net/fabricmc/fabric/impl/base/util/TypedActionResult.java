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

package net.fabricmc.fabric.impl.base.util;

public class TypedActionResult<T> {
	private final ActionResult result;
	private final T value;

	public TypedActionResult(ActionResult result, T value) {
		this.result = result;
		this.value = value;
	}

	public ActionResult getResult() {
		return this.result;
	}

	public T getValue() {
		return this.value;
	}

	public static <T> TypedActionResult<T> success(T data) {
		return new TypedActionResult(ActionResult.SUCCESS, data);
	}

	public static <T> TypedActionResult<T> consume(T data) {
		return new TypedActionResult(ActionResult.CONSUME, data);
	}

	public static <T> TypedActionResult<T> pass(T data) {
		return new TypedActionResult(ActionResult.PASS, data);
	}

	public static <T> TypedActionResult<T> fail(T data) {
		return new TypedActionResult(ActionResult.FAIL, data);
	}
}
