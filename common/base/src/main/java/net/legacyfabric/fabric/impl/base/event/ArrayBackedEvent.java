package net.legacyfabric.fabric.impl.base.event;

import net.legacyfabric.fabric.api.event.Event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

class ArrayBackedEvent<T> extends Event<T> {
	private final Class<? super T> type;
	private final Function<T[], T> invokerFactory;
	private final T dummyInvoker;
	private final Lock lock = new ReentrantLock();
	private T[] handlers;

	ArrayBackedEvent(Class<? super T> type, T dummyInvoker, Function<T[], T> invokerFactory) {
		this.type = type;
		this.dummyInvoker = dummyInvoker;
		this.invokerFactory = invokerFactory;
		update();
	}

	@SuppressWarnings("unchecked")
	void update() {
		if (handlers == null) {
			if (dummyInvoker != null) {
				invoker = dummyInvoker;
			} else {
				invoker = invokerFactory.apply((T[]) Array.newInstance(type, 0));
			}
		} else if (handlers.length == 1) {
			invoker = handlers[0];
		} else {
			invoker = invokerFactory.apply(handlers);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void register(T listener) {
		if (listener == null) {
			throw new NullPointerException("Tried to register a null listener!");
		}

		lock.lock();

		try {
			if (handlers == null) {
				handlers = (T[]) Array.newInstance(type, 1);
				handlers[0] = listener;
			} else {
				handlers = Arrays.copyOf(handlers, handlers.length + 1);
				handlers[handlers.length - 1] = listener;
			}

			update();
		} finally {
			lock.unlock();
		}
	}
}
