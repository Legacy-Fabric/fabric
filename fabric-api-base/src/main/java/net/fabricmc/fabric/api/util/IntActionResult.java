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

package net.fabricmc.fabric.api.util;

import java.util.Objects;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.fabric.impl.base.util.TypedActionResult;

/**
 * Integer specific version of {@link TypedActionResult}.
 *
 * <p>This type specific version prevent unnecessary
 * boxing and unboxing of primitive integer values.</p>
 */
public class IntActionResult {
	private final ActionResult result;
	private final int value;

	public IntActionResult(ActionResult result, int value) {
		this.result = result;
		this.value = value;
	}

	public ActionResult getResult() {
		return this.result;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		IntActionResult that = (IntActionResult) o;
		return this.value == that.value && this.result == that.result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.value);
	}

	public static IntActionResult success(int data) {
		return new IntActionResult(ActionResult.SUCCESS, data);
	}

	public static IntActionResult consume(int data) {
		return new IntActionResult(ActionResult.CONSUME, data);
	}

	public static IntActionResult pass(int data) {
		return new IntActionResult(ActionResult.PASS, data);
	}

	public static IntActionResult fail(int data) {
		return new IntActionResult(ActionResult.FAIL, data);
	}
}
