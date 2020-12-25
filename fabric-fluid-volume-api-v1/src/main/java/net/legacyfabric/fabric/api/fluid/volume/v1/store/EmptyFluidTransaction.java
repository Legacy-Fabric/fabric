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

package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;

/**
 * An {@code EmptyFluidTransaction} is a kind of transaction that
 * does not insert nor extract fluid from any of its fluid handlers.
 * It merely prints an error message and the current stacktrace.
 */
public class EmptyFluidTransaction extends FluidTransaction {
	EmptyFluidTransaction() {
		super(null, null);
	}

	/**
	 * @return {@link Fraction#ZERO}
	 */
	@Override
	public Fraction move() {
		System.err.println("Attempted to move fluid in an empty transaction");

		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			System.err.println("\tat " + e);
		}

		return Fraction.ZERO;
	}

	/**
	 * @return {@link Fraction#ZERO}
	 */
	@Override
	public Fraction move(Fraction amount) {
		System.err.println("Attempted to move fluid in an empty transaction");

		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			System.err.println("\tat " + e);
		}

		return Fraction.ZERO;
	}
}
