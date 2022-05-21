package net.legacyfabric.fabric.impl.item.group;

public interface CreativeGuiExtensions {
	void fabric_nextPage();

	void fabric_previousPage();

	int fabric_currentPage();

	boolean fabric_isButtonVisible(FabricCreativeGuiComponents.Type type);

	boolean fabric_isButtonEnabled(FabricCreativeGuiComponents.Type type);
}
