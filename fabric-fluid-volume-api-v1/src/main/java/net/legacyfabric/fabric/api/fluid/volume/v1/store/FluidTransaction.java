package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import java.util.function.BooleanSupplier;

import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;

/**
 * A wrapper around two {@link FluidStorageHandler} instances
 * aimed to simplify transferring of fluid between two containers.
 */
public class FluidTransaction {
	/**
	 * The {@link FluidStorageHandler} from which fluid should be extracted.
	 */
	private final FluidStorageHandler source;
	/**
	 * The {@link FluidStorageHandler} into which fluid should be inserted.
	 */
	private final FluidStorageHandler target;
	/**
	 * Whether the transaction should be simulated, and not actually performed.
	 */
	private boolean simulate = false;
	/**
	 * Whether the transaction should be cancelled.
	 * This implies that neither simulation, nor an action will be performed.
	 */
	private boolean disabled = false;

	FluidTransaction(FluidStorageHandler source, FluidStorageHandler target) {
		this.source = source;
		this.target = target;
	}

	/**
	 * Sets {@link #simulate} to {@code true}
	 */
	public FluidTransaction simulate() {
		this.simulate = true;
		return this;
	}

	/**
	 * Moves the maximum amount of fluid from {@link #source} to {@link #target}.
	 *
	 * @return The actual amount extracted from {@link #source} and inserted into {@link #target}
	 */
	public Fraction move() {
		return this.move(Fraction.MAX_VALUE);
	}

	/**
	 * Sets {@link #disabled} to {@code true} if the {@code booleanSupplier} returns {@code false}.
	 */
	public FluidTransaction onlyIf(BooleanSupplier booleanSupplier) {
		if (!booleanSupplier.getAsBoolean()) {
			this.disabled = true;
		}
		return this;
	}

	/**
	 * Moves a certain amount of fluid from {@link #source} to {@link #target}.
	 *
	 * @param amount The amount of fluid to transfer
	 * @return The actual amount extracted from {@link #source} and inserted into {@link #target}
	 */
	public Fraction move(Fraction amount) {
		if (this.disabled) {
			return Fraction.ZERO;
		} else if (this.simulate || !this.source.shouldPerform() || !this.target.shouldPerform()) {
			this.simulate = true;
			this.source.simulate();
			this.target.simulate();
		}
		Fraction targetMaxInput = this.target.getMaxInput();
		Fraction sourceMaxOutput = this.source.getMaxOutput();
		Fraction maxInserted = sourceMaxOutput.isLessThan(amount) ? sourceMaxOutput : amount;
		Fraction maxMove = targetMaxInput.isLessThan(maxInserted) ? targetMaxInput : maxInserted;
		if (maxMove.isNegative()) {
			return Fraction.ZERO;
		}
		return this.target.insert(this.source.extract(amount));
	}
}
