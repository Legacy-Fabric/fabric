package net.legacyfabric.fabric.impl.client.keybinding;

public interface ControlsScreenExtensions {
	void fabric_nextPage();

	void fabric_previousPage();

	int fabric_currentPage();

	boolean fabric_isButtonVisible(FabricControlsScreenComponents.Type type);

	boolean fabric_isButtonEnabled(FabricControlsScreenComponents.Type type);
}
