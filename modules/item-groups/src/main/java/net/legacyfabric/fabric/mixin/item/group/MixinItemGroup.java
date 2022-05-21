package net.legacyfabric.fabric.mixin.item.group;

import net.legacyfabric.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.itemgroup.ItemGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemGroup.class)
public class MixinItemGroup implements ItemGroupExtensions {
	@Shadow
	@Final
	@Mutable
	public static ItemGroup[] itemGroups;

	@Override
	public void fabric_expandArray() {
		ItemGroup[] tempGroups = itemGroups;
		itemGroups = new ItemGroup[itemGroups.length + 1];
		System.arraycopy(tempGroups, 0, itemGroups, 0, tempGroups.length);
	}
}
