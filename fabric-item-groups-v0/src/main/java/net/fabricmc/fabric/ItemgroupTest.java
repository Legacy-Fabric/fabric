package net.fabricmc.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ItemgroupTest implements ModInitializer {
	@Override
	public void onInitialize() {
		ItemGroup FOO = FabricItemGroupBuilder.create(new Identifier("foo:bar")).appendItems((stacks)->{
			stacks.add(new ItemStack(Items.ARROW));
			stacks.add(new ItemStack(Items.APPLE));
			stacks.add(new ItemStack(Items.ARROW));
			stacks.add(new ItemStack(Items.ARROW));
			stacks.add(new ItemStack(Items.ARROW));
		}).icon(()->new ItemStack(Items.ARMOR_STAND)).build();
		ItemGroup BAR = FabricItemGroupBuilder.create(new Identifier("bar:foo")).appendItems((stacks)->{
			stacks.add(new ItemStack(Items.CAKE));
			stacks.add(new ItemStack(Items.SKULL));
			stacks.add(new ItemStack(Items.FIREWORKS));
			stacks.add(new ItemStack(Items.ARROW));
			stacks.add(new ItemStack(Items.ARROW));
		}).icon(()->new ItemStack(Items.CARROT)).build();
	}
}
