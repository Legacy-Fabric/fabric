package net.legacyfabric.fabric.api.fluid.volume.v1.store;

import net.legacyfabric.fabric.api.fluid.volume.v1.FluidType;
import net.legacyfabric.fabric.api.fluid.volume.v1.fraction.Fraction;
import net.legacyfabric.fabric.api.fluid.volume.v1.util.Side;

/**
 * A wrapper around a {@link FluidStorage} that allows for various
 * transfer functions.
 */
public class FluidStorageHandler {
	/**
	 * The {@link FluidStorage} being inserted into or extracted from
	 */
	private final FluidStorage storage;
	/**
	 * The {@link FluidType} being transferred
	 */
	private final FluidType fluidType;
	/**
	 * Whether the transaction is valid. This is assigned with a {@link FluidStorage#isValid(FluidType)} call
	 */
	private final boolean valid;
	/**
	 * Whether the transaction should only be simulated, and not actually performed
	 */
	private boolean simulate = false;
	/**
	 * The side from which the transaction is taking place. {@link Side#UNKNOWN} specifies no specific side
	 */
	private Side side = Side.UNKNOWN;

	public FluidStorageHandler(FluidStorage storage, FluidType fluidType) {
		this.storage = storage;
		this.fluidType = fluidType;
		this.valid = storage.isValid(fluidType);
	}

	/**
	 * Sets {@link #simulate} to {@code true}
	 */
	public FluidStorageHandler simulate() {
		this.simulate = true;
		return this;
	}

	/**
	 * @return Whether an action should actually be performed
	 */
	public boolean shouldPerform() {
		return !this.simulate;
	}

	/**
	 * Extracts a certain amount of fluid of {@link #fluidType} from {@link #storage}
	 *
	 * @param amount the amount of fluid to be extracted
	 * @return the actual amount that was extracted from {@link #storage}, depending on its fluid rate and capacity
	 */
	public Fraction extract(Fraction amount) {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction stored = this.storage.getStored(this.fluidType, this.side);
		Fraction maxExtracted = amount.isGreaterThan(stored) ? amount : stored;
		Fraction maxOutput = this.storage.getMaxOutput(this.fluidType, this.side);
		Fraction extracted = maxExtracted.isGreaterThan(maxOutput) ? maxOutput : maxExtracted;
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, stored.withSubtraction(extracted));
		}
		return extracted;
	}

	/**
	 * Inserts a certain amount of fluid of {@link #fluidType} from {@link #storage}
	 *
	 * @param amount the amount of fluid to be inserted
	 * @return the actual amount that was inserted into {@link #storage}, depending on its fluid rate and capacity
	 */
	public Fraction insert(Fraction amount) {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction stored = this.storage.getStored(this.fluidType, this.side);
		Fraction maxMinus = this.storage.getMaxFluidVolume(this.fluidType, this.side).withSubtraction(stored);
		Fraction maxInserted = maxMinus.isGreaterThan(amount) ? amount : maxMinus;
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction inserted = maxInserted.isGreaterThan(maxInput) ? maxInput : maxInserted;
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, stored.withAddition(inserted));
		}
		return inserted;
	}

	/**
	 * Sets the amount of fluid of {@link #fluidType} in {@link #storage}.
	 * The amount is clamped between the maximum fluid volume and zero.
	 *
	 * @param amount the amount of fluid
	 * @return whether the transaction was successful
	 */
	public boolean set(Fraction amount) {
		if (!this.valid) {
			return false;
		}
		if (amount.isNegative()) {
			amount = Fraction.ZERO;
		}
		Fraction max = this.storage.getMaxFluidVolume(this.fluidType, this.side);
		if (amount.isGreaterThan(max)) {
			amount = max;
		}
		if (this.shouldPerform()) {
			this.storage.setStored(this.fluidType, this.side, amount);
		}
		return true;
	}

	/**
	 * Returns the maximum amount of fluid that can be inserted into {@link #storage}.
	 * This value is either the maximum capacity minus the stored fluid, or the maximum fluid insertion rate.
	 * The lowest of these two values is the determined maximum input.
	 *
	 * @return The maximum amount of fluid that can be inserted into {@link #storage}.
	 */
	public Fraction getMaxInput() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction maxInput = this.storage.getMaxInput(this.fluidType, this.side);
		Fraction maxInserted = this.storage.getMaxFluidVolume(this.fluidType, this.side).withSubtraction(this.storage.getStored(this.fluidType, this.side));
		return maxInput.isLessThan(maxInserted) ? maxInput : maxInserted;
	}

	/**
	 * Returns the maximum amount of fluid that can be inserted into {@link #storage}.
	 * This value is either the stored fluid, or the maximum fluid extraction rate.
	 * The lowest of these two values is the determined maximum input.
	 *
	 * @return The maximum amount of fluid that can be inserted into {@link #storage}.
	 */
	public Fraction getMaxOutput() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		Fraction maxOutput = this.storage.getMaxOutput(this.fluidType, this.side);
		Fraction maxExtracted = this.storage.getStored(this.fluidType, this.side);
		return maxOutput.isLessThan(maxExtracted) ? maxOutput : maxExtracted;
	}

	/**
	 * @return the amount of fluid of {@link #fluidType} stored in {@link #storage}.
	 */
	public Fraction getStored() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		return this.storage.getStored(this.fluidType, this.side);
	}

	/**
	 * @return the maximum capacity of fluid of {@link #fluidType} in {@link #storage}
	 */
	public Fraction getMaxFluidVolume() {
		if (!this.valid) {
			return Fraction.ZERO;
		}
		return this.storage.getMaxFluidVolume(this.fluidType, this.side);
	}

	/**
	 * Creates a {@link FluidTransaction} to help moving fluid of {@link #fluidType}.
	 * An {@link EmptyFluidTransaction} is returned if the transaction is deemed not valid.
	 * This is caused if either of the handlers do not support inserted fluid of {@link #fluidType}.
	 *
	 * @param target The target handler
	 * @return a fluid transaction
	 */
	public FluidTransaction into(FluidStorageHandler target) {
		if (!this.valid || !target.valid) {
			return new EmptyFluidTransaction();
		}
		return new FluidTransaction(this, target);
	}

	/**
	 * Sets the side to insert into or extract from
	 */
	public FluidStorageHandler side(Side side) {
		this.side = side;
		return this;
	}

	/**
	 * Consumes a certain amount of fluid.
	 *
	 * @param amount the amount of fluid to consume
	 * @return whether the consumption was a success
	 */
	public boolean consume(Fraction amount) {
		return this.set(this.getStored().withSubtraction(amount));
	}
}
