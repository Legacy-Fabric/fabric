package net.legacyfabric.fabric.mixin.resource.loader.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.Block;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.impl.resource.loader.ItemModelRegistryImpl;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ItemModelRegistryImpl.Registrar {
	@Shadow
	protected abstract void addModel(Item item, int metadata, String id);

	@Shadow
	protected abstract void addModel(Item item, String id);

	@Shadow
	protected abstract void addModel(Block block, String id);

	@Shadow
	protected abstract void addModel(Block block, int metadata, String id);

	@Override
	public void fabric_register() {
		ItemModelRegistryImpl.ITEMS_WITHOUT_META.forEach(pair -> {
			this.addModel(pair.getObject(), pair.getModel().toString());
		});
		ItemModelRegistryImpl.BLOCKS_WITHOUT_META.forEach(pair -> {
			this.addModel(pair.getObject(), pair.getModel().toString());
		});
		ItemModelRegistryImpl.ITEMS_WITH_META.forEach(triad -> {
			this.addModel(triad.getObject(), triad.getMetadata(), triad.getModel().toString());
		});
		ItemModelRegistryImpl.BLOCKS_WITH_META.forEach(triad -> {
			this.addModel(triad.getObject(), triad.getMetadata(), triad.getModel().toString());
		});
	}
}
