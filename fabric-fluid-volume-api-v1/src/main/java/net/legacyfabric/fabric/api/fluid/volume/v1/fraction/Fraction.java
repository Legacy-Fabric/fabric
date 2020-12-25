package net.legacyfabric.fabric.api.fluid.volume.v1.fraction;

import java.util.Objects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

/**
 * Immutable, full-resolution rational number representation.
 */
@SuppressWarnings("unused")
public class Fraction extends Number implements Comparable<Fraction> {
	public static final Fraction ZERO = Fraction.of(0, 0, 1);
	public static final Fraction ONE_THIRD = Fraction.of(0, 1,3);
	public static final Fraction HALF = Fraction.of(0, 1, 2);
	public static final Fraction ONE = Fraction.of(1, 0, 1);
	public static final Fraction ONE_HUNDRED = Fraction.of(100, 0, 1);
	public static final Fraction ONE_THOUSAND = Fraction.of(1000, 0, 1);
	public static final Fraction MAX_VALUE = Fraction.of(Long.MAX_VALUE, 0, 1);

	protected long whole;
	protected long numerator;
	protected long divisor;

	/**
	 * Constructs a new fraction with value of zero.
	 * Generally better to use {@link #ZERO} instead.
	 */
	public Fraction() {
		this(0, 0, 1);
	}

	public Fraction(long whole, long numerator, long divisor) {
		this.validate(whole, numerator, divisor);
		this.whole = whole;
		this.numerator = numerator;
		this.divisor = divisor;
		this.normalize();
	}

	public Fraction(long numerator, long divisor) {
		this.validate(0, numerator, divisor);
		this.whole = numerator / divisor;
		this.numerator = numerator - this.whole * divisor;
		this.divisor = divisor;
		this.normalize();
	}

	public Fraction(long whole) {
		this(whole, 0, 1);
	}

	public Fraction(Fraction template) {
		this(template.whole(), template.numerator(), template.divisor());
	}

	public Fraction(CompoundTag tag) {
		this();
		this.readTagInner(tag);
	}

	public Fraction(PacketByteBuf buf) {
		this();
		this.readBufInner(buf);
	}

	/**
	 * The whole-number portion of this fraction.
	 *
	 * If this fraction is negative, both {@code whole()} and {@link #numerator()}
	 * will be zero or negative.
	 *
	 * @return The whole-number portion of this fraction
	 */
	public final long whole() {
		return this.whole;
	}

	/**
	 * The fractional portion of this fraction, or zero if the fraction
	 * represents a whole number.<p>
	 *
	 * If this fraction is negative, both {@link #whole()} and {@code numerator()}
	 * will be zero or negative.<p>
	 *
	 * The absolute values of {@code numerator()} will always be zero
	 * or less than the value of {@link #divisor()}. Whole numbers are
	 * always fully represented in {@link #whole()}.
	 *
	 * @return The whole-number portion of this fraction
	 */
	public final long numerator() {
		return this.numerator;
	}

	/**
	 * The denominator for the fractional portion of this fraction.
	 * Will always be >= 1.
	 *
	 * @return the denominator for the fractional portion of this fraction
	 */
	public final long divisor() {
		return this.divisor;
	}

	protected final void readBufInner(PacketByteBuf buf) {
		this.whole = buf.readVarLong();
		this.numerator = buf.readVarLong();
		this.divisor = buf.readVarLong();
		this.normalize();
	}

	protected final void readTagInner(CompoundTag tag) {
		this.whole = tag.getLong("whole");
		this.numerator = tag.getLong("numerator");
		this.divisor = Math.max(1, tag.getLong("denominator"));
		this.normalize();
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putLong("whole", this.whole);
		tag.putLong("numerator", this.numerator);
		tag.putLong("denominator", this.divisor);
		return tag;
	}

	public PacketByteBuf toBuf(PacketByteBuf buf) {
		buf.writeVarLong(this.whole);
		buf.writeVarLong(this.numerator);
		buf.writeVarLong(this.divisor);
		return buf;
	}

	@Override
	public final boolean equals(Object val) {
		if (!(val instanceof Fraction)) {
			return false;
		}

		return this.compareTo((Fraction) val) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.whole, this.numerator, this.divisor);
	}

	protected final void validate(long whole, long numerator, long divisor) {
		if (divisor < 1) {
			throw new IllegalArgumentException("Fraction divisor must be >= 1");
		}
	}

	@Override
	public final String toString() {
		return String.format("%d and %d / %d, approx: %f", this.whole, this.numerator, this.divisor, this.toDouble());
	}

	protected final void normalize() {
		if (Math.abs(this.numerator) >= this.divisor) {
			final long w = this.numerator / this.divisor;
			this.whole += w;
			this.numerator -= w * this.divisor;
		}

		if (this.numerator == 0) {
			this.divisor = 1;
			return;
		}

		// keep signs consistent
		if (this.whole < 0) {
			if (this.numerator > 0) {
				this.whole += 1;
				this.numerator -= this.divisor;
			}
		} else if (this.numerator < 0) {
			if (this.whole > 0) {
				this.whole -= 1;
				this.numerator += this.divisor;
			}
		}

		// remove powers of two bitwise
		final int twos = Long.numberOfTrailingZeros(this.numerator | this.divisor);

		if (twos > 0) {
			this.numerator >>= twos;
			this.divisor >>= twos;
		}

		// use conventional gcd for rest
		final long gcd = this.gcd(Math.abs(this.numerator), this.divisor);

		if (gcd != this.divisor) {
			this.numerator /= gcd;
			this.divisor /= gcd;
		}
	}

	protected final long gcd(long a, long b) {
		while (b != 0) {
			final long t = b;
			b = a % b;
			a = t;
		}

		return a;
	}

	/**
	 * Intended for user display. Result may be approximate due to floating point error.
	 *
	 * @param units Fraction of one that counts as 1 in the result. Must be >= 1.
	 * @return Current value scaled so that that 1.0 = one of the given units
	 */
	public final double toDouble(long units) {
		// start with unit scale
		final double base = (double) this.numerator() / (double) this.divisor() + this.whole();

		// scale to requested unit
		return units == 1 ? base : base / units;
	}

	/**
	 * Intended for user display. Result may be approximate due to floating point error.
	 *
	 * @return This fraction as a {@code double} primitive
	 */
	public final double toDouble() {
		return this.toDouble(1);
	}

	/**
	 * Returns the number of units that is less than or equal to the given unit.
	 * Make be larger than this if value is not evenly divisible .
	 *
	 * @param divisor Fraction of one bucket that counts as 1 in the result. Must be >= 1.
	 * @return Number of units within current volume.
	 */
	public final long toLong(long divisor) {
		if (divisor < 1) {
			throw new IllegalArgumentException("RationalNumber divisor must be >= 1");
		}

		final long base = this.whole() * divisor;

		if (this.numerator() == 0) {
			return base;
		} else if (this.divisor() == divisor) {
			return base + this.numerator();
		} else {
			return base + this.numerator() * divisor / this.divisor();
		}
	}

	/**
	 * Test if this fraction is exactly zero.
	 *
	 * @return {@code true} if this fraction is exactly zero
	 */
	public final boolean isZero() {
		return this.whole() == 0 && this.numerator() == 0;
	}

	/**
	 * Test if this fraction is a negative number.
	 *
	 * @return {@code true} if this fraction is a negative number
	 */
	public final boolean isNegative() {
		return this.whole() < 0 || (this.whole() == 0 && this.numerator() < 0);
	}

	@Override
	public final int compareTo(Fraction o) {
		final int result = Long.compare(this.whole(), o.whole());
		return result == 0 ? Long.compare(this.numerator() * o.divisor(), o.numerator() * this.divisor()) : result;
	}

	@Override
	public int intValue() {
		return (int) this.longValue();
	}

	@Override
	public long longValue() {
		return this.toLong(1);
	}

	@Override
	public float floatValue() {
		return (float) this.doubleValue();
	}

	@Override
	public double doubleValue() {
		return this.toDouble();
	}

	public final boolean isGreaterThan(Fraction other) {
		return this.compareTo(other) > 0;
	}

	public final boolean isGreaterThanOrEqual(Fraction other) {
		return this.compareTo(other) >= 0;
	}

	public final boolean isLessThan(Fraction other) {
		return this.compareTo(other) < 0;
	}

	public final boolean isLessThanOrEqual(Fraction other) {
		return this.compareTo(other) <= 0;
	}

	/**
	 * The smallest whole number that is greater than or equal to the
	 * rational number represented by this fraction.
	 *
	 * @return the smallest whole number greater than or equal to this fraction
	 */
	public final long ceil() {
		return this.numerator() == 0 || this.whole() < 0 ? this.whole() : this.whole() + 1;
	}

	/**
	 * Returns a new value equal to this fraction multiplied by -1.
	 *
	 * @return A new fraction equal to this fraction multiplied by -1
	 */
	public final Fraction toNegated() {
		return Fraction.of(-this.whole(), -this.numerator(), this.divisor());
	}

	/**
	 * Returns a new value equal to this value less the given parameter.
	 *
	 * This method is allocating and for frequent and repetitive operations
	 * it will be preferable to use a mutable fraction instance.
	 *
	 * @param diff value to be subtracted from this value
	 * @return a new value equal to this value less the given parameter
	 */
	public final Fraction withSubtraction(Fraction diff) {
		final MutableFraction f = new MutableFraction(this);
		f.subtract(diff);
		return f.toImmutable();
	}

	/**
	 * Returns a new value equal to this value plus the given parameter.
	 *
	 * This method is allocating and for frequent and repetitive operations
	 * it will be preferable to use a mutable fraction instance.
	 *
	 * @param diff value to be added to this value
	 * @return a new value equal to this value plus the given parameter
	 */
	public final Fraction withAddition(Fraction diff) {
		final MutableFraction f = new MutableFraction(this);
		f.add(diff);
		return f.toImmutable();
	}

	/**
	 * Returns a new value equal to this value multiplied by the given parameter.
	 *
	 * This method is allocating and for frequent and repetitive operations
	 * it will be preferable to use a mutable fraction instance.
	 *
	 * @param diff value to be multiplied by this value
	 * @return a new value equal to this value multiplied by the given parameter
	 */
	public final Fraction withMultiplication(Fraction diff) {
		final MutableFraction f = new MutableFraction(this);
		f.multiply(diff);
		return f.toImmutable();
	}

	public static Fraction of(long whole, long numerator, long divisor) {
		return new Fraction(whole, numerator, divisor);
	}

	public static Fraction of(long numerator, long divisor) {
		return new Fraction(numerator, divisor);
	}

	public static Fraction of(long whole) {
		return new Fraction(whole);
	}

}
