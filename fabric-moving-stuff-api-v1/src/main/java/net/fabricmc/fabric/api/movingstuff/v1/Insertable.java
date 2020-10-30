package net.fabricmc.fabric.api.movingstuff.v1;

import net.minecraft.util.math.Direction;

/**
 * Base class for objects that can have a certain thing inserted into them.
 *
 * @param <T> thing
 */
public interface Insertable<T> {
	/**
	 * Inserts a certain amount of thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 */
	void insert(Direction fromSide, T thing, int amount);

	/**
	 * Checks whether a certain amount of thing can be inserted into this insertable
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @return whether the insertion is possible
	 */
	boolean canInsert(Direction fromSide, T thing, int amount);

	/**
	 * Attempt to insert thing into this insertable.
	 *
	 * @param fromSide the side from which to insert.
	 * @param thing the type of thing to insert.
	 * @param amount how much thing to insert.
	 * @param simulate whether the action should be simulated and not actually performed.
	 * @return whether the insertion was/would be successful.
	 */
	default boolean tryInsertFluid(Direction fromSide, T thing, int amount, boolean simulate) {
		if (this.canInsert(fromSide, thing, amount)) {
			if (simulate) {
				this.insert(fromSide, thing, amount);
			}

			return true;
		}

		return false;
	}
}
