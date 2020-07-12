package net.fabricmc.fabric.api.blockrenderlayer.v1;

import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;

public interface BlockRenderLayerMap {
	BlockRenderLayerMap INSTANCE = BlockRenderLayerMapImpl.INSTANCE;

	void putBlock(Block block, RenderLayer renderLayer);

	void putBlocks(RenderLayer renderLayer, Block... blocks);

	void putItem(Item item, RenderLayer renderLayer);

	void putItems(RenderLayer renderLayer, Item... items);

//	void putFluid(Fluid fluid, RenderLayer renderLayer);
//
//	void putFluids(RenderLayer renderLayer, Fluid... fluids);
}
