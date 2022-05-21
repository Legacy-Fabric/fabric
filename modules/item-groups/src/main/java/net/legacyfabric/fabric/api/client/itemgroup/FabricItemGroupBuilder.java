package net.legacyfabric.fabric.api.client.itemgroup;

import net.legacyfabric.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.Item;
import net.minecraft.item.itemgroup.ItemGroup;
import net.minecraft.util.Identifier;

public class FabricItemGroupBuilder {
	private Identifier identifier;
	private Item icon;

	public ItemGroup build() {
		((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
		int index = ItemGroup.itemGroups.length - 1;
		return ItemGroup.itemGroups[index] = new ItemGroup(index, String.format("%s.%s", identifier.getNamespace(), identifier.getPath())) {
			@Override
			public Item getIconItem() {
				return icon;
			}
		};
	}
}
