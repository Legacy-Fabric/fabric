package net.fabricmc.fabric.api.content.registry.v1;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class FabricToolItem extends Item {
	protected final float miningSpeed;
	protected final float attackDamage;
	protected Material effectiveMaterial;
	protected final ToolMaterial material;

	protected FabricToolItem(float attackDamage, ToolMaterial material, Material effectiveMaterial) {
		this.material = material;
		this.effectiveMaterial = effectiveMaterial;
		this.maxCount = 1;
		this.setMaxDamage(material.getMaxDurability());
		this.miningSpeed = material.getMiningSpeedMultiplier();
		this.attackDamage = attackDamage + material.getAttackMultiplier();
	}

	protected FabricToolItem(float attackDamage, ToolMaterial material) {
		this.material = material;
		this.maxCount = 1;
		this.setMaxDamage(material.getMaxDurability());
		this.miningSpeed = material.getMiningSpeedMultiplier();
		this.attackDamage = attackDamage + material.getAttackMultiplier();
	}

	@Override
	public float getMiningSpeedForBlock(ItemStack stack, Block block) {
		return block.getMaterial() == this.effectiveMaterial ? this.miningSpeed : 1.0F;
	}

	@Override
	public boolean method_8263(ItemStack stack, LivingEntity entity1, LivingEntity entity2) {
		stack.method_8335(2, entity2);
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public final boolean isHandheld() {
		return true;
	}

	public final ToolMaterial getMaterial() {
		return material;
	}

	public final String getMaterialAsString() {
		return this.material.toString();
	}

	@Override
	public boolean method_8258(ItemStack stack, World world, Block block, BlockPos pos, LivingEntity entity) {
		if ((double)block.getStrength(world, pos) != 0.0D) {
			stack.method_8335(1, entity);
		}

		return true;
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().get().getItem() == ingredient.getItem();
	}

	@Override
	public int getMaxDamage() {
		return this.material.getMaxDurability();
	}

	@Override
	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifierMap() {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifierMap();
		multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Tool modifier", this.attackDamage, 0));
		return multimap;
	}

	@Override
	public abstract boolean isEffectiveOn(Block block);
}
