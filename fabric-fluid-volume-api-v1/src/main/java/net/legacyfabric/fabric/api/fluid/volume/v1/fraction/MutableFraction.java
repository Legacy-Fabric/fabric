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

package net.legacyfabric.fabric.api.fluid.volume.v1.fraction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public final class MutableFraction extends Fraction {
	public MutableFraction() {
		super();
	}

	public MutableFraction(long whole) {
		super(whole, 0, 1);
	}

	public MutableFraction(long numerator, long divisor) {
		super(numerator, divisor);
	}

	public MutableFraction(long whole, long numerator, long divisor) {
		super(whole, numerator, divisor);
	}

	public MutableFraction(Fraction template) {
		super(template.whole(), template.numerator(), template.divisor());
	}

	public MutableFraction(CompoundTag tag) {
		super(tag);
	}

	public MutableFraction(PacketByteBuf buf) {
		super(buf);
	}

	public MutableFraction set(long whole) {
		return this.set(whole, 0, 1);
	}

	public MutableFraction set(long numerator, long divisor) {
		this.validate(0, numerator, divisor);
		this.whole = numerator / divisor;
		this.numerator = numerator - this.whole * divisor;
		this.divisor = divisor;
		return this;
	}

	public MutableFraction set(long whole, long numerator, long divisor) {
		this.validate(whole, numerator, divisor);
		this.whole = whole;
		this.numerator = numerator;
		this.divisor = divisor;
		return this;
	}

	public MutableFraction set(Fraction template) {
		this.whole = template.whole();
		this.numerator = template.numerator();
		this.divisor = template.divisor();
		return this;
	}

	public MutableFraction add(Fraction val) {
		return this.add(val.whole(), val.numerator(), val.divisor());
	}

	public MutableFraction add(long whole) {
		return this.add(whole, 0, 1);
	}

	public MutableFraction add(long numerator, long divisor) {
		return this.add(0, numerator, divisor);
	}

	public MutableFraction add(long whole, long numerator, long divisor) {
		this.validate(whole, numerator, divisor);
		this.whole += whole;

		if (Math.abs(numerator) >= divisor) {
			final long w = numerator / divisor;
			this.whole += w;
			numerator -= w * divisor;
		}

		final long n = this.numerator * divisor + numerator * this.divisor;

		if (n == 0) {
			this.numerator = 0;
			this.divisor = 1;
		} else {
			this.numerator = n;
			this.divisor = divisor * this.divisor;
			this.normalize();
		}

		return this;
	}

	public MutableFraction multiply(Fraction val) {
		return this.multiply(val.whole(), val.numerator(), val.divisor());
	}

	public MutableFraction multiply(long whole) {
		this.numerator *= whole;
		this.whole *= whole;
		this.normalize();
		return this;
	}

	public MutableFraction multiply(long numerator, long divisor) {
		return this.multiply(0, numerator, divisor);
	}

	public MutableFraction multiply(long whole, long numerator, long divisor) {
		if (numerator == 0) {
			return this.multiply(whole);
		}

		this.validate(whole, numerator, divisor);

		// normalize fractional part
		if (Math.abs(numerator) >= divisor) {
			final long w = numerator / divisor;
			whole += w;
			numerator -= w * divisor;
		}

		// avoids a division later to factor out common divisor from the two steps that follow this
		final long numeratorProduct = numerator * this.numerator;
		this.numerator *= divisor;
		numerator *= this.divisor;

		this.divisor *= divisor;
		this.numerator = this.numerator * whole + numerator * this.whole + numeratorProduct;
		this.whole *= whole;
		this.normalize();

		return this;
	}

	public MutableFraction subtract(Fraction val) {
		return this.add(-val.whole(), -val.numerator(), val.divisor());
	}

	public MutableFraction subtract(long whole) {
		return this.add(-whole, 0, 1);
	}

	public MutableFraction subtract(long numerator, long divisor) {
		return this.add(0, -numerator, divisor);
	}

	public MutableFraction negate() {
		this.numerator = -this.numerator;
		this.whole = -this.whole;
		return this;
	}

	public void roundDown(long divisor) {
		if (this.divisor != divisor) {
			this.set(this.whole, this.numerator * divisor / this.divisor, divisor);
		}
	}

	public Fraction toImmutable() {
		return Fraction.of(this.whole(), this.numerator(), this.divisor());
	}
}
