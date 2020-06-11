package net.fabricmc.fabric.impl.base.util;

import net.minecraft.item.ItemStack;

public class TypedActionResult<T> {
	private final ActionResult result;
	private final T value;

	public TypedActionResult(ActionResult result, T value) {
		this.result = result;
		this.value = value;
	}

	public ActionResult getResult() {
		return this.result;
	}

	public T getValue() {
		return this.value;
	}

	public static <T> TypedActionResult<T> success(T data) {
		return new TypedActionResult(ActionResult.SUCCESS, data);
	}

	public static <T> TypedActionResult<T> consume(T data) {
		return new TypedActionResult(ActionResult.CONSUME, data);
	}

	public static <T> TypedActionResult<T> pass(T data) {
		return new TypedActionResult(ActionResult.PASS, data);
	}

	public static <T> TypedActionResult<T> fail(T data) {
		return new TypedActionResult(ActionResult.FAIL, data);
	}
}
