package net.fabricmc.fabric.api.transaction.v1;

import net.minecraft.util.math.Direction;

/**
 * Specifies a class that can participate in Transactions
 *
 * @param <T> the type of value that can be transferred
 */
public interface TransactionParticipant<T> {
	/**
	 * Specifies whether this participant can contain more {@link T}.
	 *
	 * @param direction the direction from which the transaction is taking place
	 * @param value the value that is being inserted by the transaction
	 * @return whether this participant can contain more {@link T}
	 */
	boolean canInsert(Direction direction, T value);

	/**
	 * Specifies whether this participant can remove a certain amount of {@link T}.
	 *
	 * @param direction the direction from which the transaction is taking place
	 * @param value the value that is being requested by the transaction
	 * @return whether this participant can remove a certain amount of {@link T}
	 */
	boolean canRemove(Direction direction, T value);

	/**
	 * Gets the total amount of {@link T} contained within this participant
	 *
	 * @return the total amount of {@link T} contained
	 */
	T getCurrent();
}
