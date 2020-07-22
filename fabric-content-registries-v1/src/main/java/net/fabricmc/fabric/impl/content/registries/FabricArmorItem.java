package net.fabricmc.fabric.impl.content.registries;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.class_1748;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.content.registry.v1.ArmorMaterial;

public class FabricArmorItem extends Item {
	private static final int[] BASE_DURABILITY = new int[]{11, 16, 15, 13};
	public static final String[] EMPTY = new String[]{"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
	private static final DispenserBehavior ARMOR_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			BlockPos blockPos = pointer.getBlockPos().offset(DispenserBlock.method_812(pointer.method_4894()));
			int i = blockPos.getX();
			int j = blockPos.getY();
			int k = blockPos.getZ();
			Box box = new Box(i, j, k, i + 1, j + 1, k + 1);
			List<LivingEntity> list = pointer.getWorld().method_318(LivingEntity.class, box, Predicates.and(class_1748.field_7425, new class_1748.class_1749(stack)));
			if (list.size() > 0) {
				LivingEntity livingEntity = list.get(0);
				int l = livingEntity instanceof PlayerEntity ? 1 : 0;
				int m = MobEntity.method_7210(stack);
				ItemStack itemStack = stack.copy();
				itemStack.count = 1;
				livingEntity.method_7011(m - l, itemStack);
				if (livingEntity instanceof MobEntity) {
					((MobEntity)livingEntity).method_7187(m, 2.0F);
				}

				--stack.count;
				return stack;
			} else {
				return super.dispenseSilently(pointer, stack);
			}
		}
	};
	public final int slot;
	public final int protection;
	public final int materialId;
	private final ArmorMaterial material;

	public FabricArmorItem(ArmorMaterial material, int materialId, int slot) {
		this.material = material;
		this.slot = slot;
		this.materialId = materialId;
		this.protection = material.getProtection(slot);
		this.setMaxDamage(material.getDurability(slot));
		this.maxCount = 1;
		this.setItemGroup(ItemGroup.COMBAT);
		DispenserBlock.SPECIAL_ITEMS.put(this, ARMOR_DISPENSER_BEHAVIOR);
	}

	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	public ArmorMaterial getMaterial() {
		return this.material;
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient() == ingredient.getItem();
	}

	public ItemStack method_8260(ItemStack stack, World world, PlayerEntity player) {
		int i = MobEntity.method_7210(stack) - 1;
		ItemStack itemStack = player.method_7177(i);
		if (itemStack == null) {
			player.method_7011(i, stack.copy());
			stack.count = 0;
		}

		return stack;
	}
}
