/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.blockrenderlayer;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

public class BlockRenderLayerMapImpl implements BlockRenderLayerMap {
	public static Map<Block, RenderLayer> blockRenderLayerMap = Maps.newHashMap();
	public static Map<Item, RenderLayer> itemRenderLayerMap = Maps.newHashMap();

	private BlockRenderLayerMapImpl() {
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

		blockRenderLayerMap.put(block, renderLayer);
	}

	@Override
	public void putBlocks(RenderLayer renderLayer, Block... blocks) {
		for (Block block: blocks) {
			this.putBlock(block, renderLayer);
		}
	}

	@Override
	public void putItem(Item item, RenderLayer renderLayer) {
		if (item == null) {
			throw new IllegalArgumentException("Request to map null item to ItemRenderLayer");
		}

		if (renderLayer == null) {
			throw new IllegalArgumentException("Request to map item " + item.toString() + " to null ItemRenderLayer");
		}

		itemRenderLayerMap.put(item, renderLayer);
	}

	@Override
	public void putItems(RenderLayer renderLayer, Item... items) {
		for (Item item: items) {
			this.putItem(item, renderLayer);
		}
	}
}
