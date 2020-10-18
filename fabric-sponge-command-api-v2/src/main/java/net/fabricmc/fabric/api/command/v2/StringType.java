package net.fabricmc.fabric.api.command.v2;

/**
 * Specifies a method to parse {@link String}s in a command.
 */
public enum StringType {
	SINGLE_WORD(false),
	GREEDY_PHRASE(true),
	;

	private final boolean all;

	StringType(boolean all) {
		this.all = all;
	}

	public boolean isAll() {
		return this.all;
	}
}
