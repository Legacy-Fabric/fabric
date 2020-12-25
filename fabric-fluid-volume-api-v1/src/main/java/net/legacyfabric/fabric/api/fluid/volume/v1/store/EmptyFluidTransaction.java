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
