package net.fabricmc.fabric.impl.base.util;

public enum ActionResult {
	SUCCESS,
	CONSUME,
	PASS,
	FAIL;

	ActionResult() {
	}

	public boolean isAccepted() {
		return this == SUCCESS || this == CONSUME;
	}

	public boolean shouldSwingHand() {
		return this == SUCCESS;
	}
}
