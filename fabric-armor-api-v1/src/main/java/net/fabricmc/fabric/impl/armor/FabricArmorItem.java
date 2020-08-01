package net.fabricmc.fabric.impl.armor;

import java.util.Random;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.armor.v1.ArmorMaterial;

public class FabricArmorItem extends ArmorItem {
	private static final int[] BASE_DURABILITY = new int[]{11, 16, 15, 13};
	private final ArmorMaterial material;
	private final int slotId;

	public FabricArmorItem(ArmorMaterial material, EquipmentSlot slot) {
		super(null, new Random().nextInt(16777215), slot.getSlotId());
		this.material = material;
		this.slotId = slot.getSlotId();
		this.setMaxDamage(BASE_DURABILITY[this.slotId] * material.getDurabilityMultiplier());
	}

	public ArmorMaterial getArmorMaterial() {
		return this.material;
	}

	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.getById(this.slotId);
	}

	@Override
	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public int getMaxDamage() {
		return this.material.getDurabilityMultiplier();
	}

	@Override
	public int getMaxCount() {
		return 1;
	}

	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem() || super.canRepair(stack, ingredient);
	}

	@Deprecated
	@Override
	public stack getMaterial() {
		return null;
	}

	@Deprecated
	@Override
	public int method_8257(ItemStack itemStack, int i) {
		return i;
	}

	@Deprecated
	@Override
	public int method_8169(ItemStack itemStack) {
		return -1;
	}

	@Deprecated
	@Override
	public void method_8171(ItemStack stack) {
	}

	@Deprecated
	@Override
	public void method_8170(ItemStack itemStack, int i) {
	}
}
