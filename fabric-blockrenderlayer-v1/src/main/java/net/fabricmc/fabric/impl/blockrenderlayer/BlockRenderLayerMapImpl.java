package net.fabricmc.fabric.impl.blockrenderlayer;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

import java.util.Map;
import java.util.function.BiConsumer;

public class BlockRenderLayerMapImpl implements BlockRenderLayerMap {

	public static Map<Block, RenderLayer> blockRenderLayerMap = Maps.newHashMap();
	public static Map<Item, RenderLayer> itemRenderLayerMap = Maps.newHashMap();

	private static BiConsumer<Block, RenderLayer> blockHandler = (b, l) -> blockRenderLayerMap.put(b, l);
	private static BiConsumer<Item, RenderLayer> itemHandler = (i, l) -> itemRenderLayerMap.put(i, l);

	private BlockRenderLayerMapImpl(){
	}

	public static final BlockRenderLayerMapImpl INSTANCE = new BlockRenderLayerMapImpl();

	@Override
	public void putBlock(Block block, RenderLayer renderLayer) {
		if (block == null) {
			throw new IllegalArgumentException("Request to map null block to BlockRenderLayer");
		}
		if (renderLayer == null) {
			throw new IllegalArgumentException("Request to map block " + block.toString() + " to null BlockRenderLayer");
		}
		blockHandler.accept(block, renderLayer);
	}

	@Override
	public void putBlocks(RenderLayer renderLayer, Block... blocks) {
		for(Block block: blocks){
			this.putBlock(block, renderLayer);
		}
	}

	@Override
	public void putItem(Item item, RenderLayer renderLayer) {
		if(item == null) {
			throw new IllegalArgumentException("Request to map null item to ItemRenderLayer");
		}
		if(renderLayer == null) {
			throw new IllegalArgumentException("Request to map item " + item.toString() + " to null ItemRenderLayer");
		}
		itemHandler.accept(item, renderLayer);
	}

	@Override
	public void putItems(RenderLayer renderLayer, Item... items) {
		for(Item item: items){
			this.putItem(item, renderLayer);
		}
	}

	public static void initialize(BiConsumer<Block, RenderLayer> blockHandlerIn) {
		BiConsumer<Item, RenderLayer> itemHandlerIn = (item, renderLayer) -> blockHandlerIn.accept(Block.getBlockFromItem(item), renderLayer);

		blockRenderLayerMap.forEach(blockHandlerIn);
		itemRenderLayerMap.forEach(itemHandlerIn);

		blockHandler = blockHandlerIn;
		itemHandler = itemHandlerIn;
	}
}
