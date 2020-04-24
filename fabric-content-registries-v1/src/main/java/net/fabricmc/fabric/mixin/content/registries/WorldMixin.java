package net.fabricmc.fabric.mixin.content.registries;

import net.fabricmc.fabric.api.content.registries.v1.BlockRegistry;
import net.fabricmc.fabric.api.content.registries.v1.ItemRegistry;
import net.fabricmc.fabric.impl.content.registries.ContentRegistryImpl;
import net.minecraft.class_635;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

@Mixin(World.class)
public class WorldMixin {


	@Inject(at=@At("TAIL"), method = "<init>")
	public void e(class_635 arg, LevelProperties levelProperties, Dimension dimension, Profiler profiler, boolean client, CallbackInfo ci){
		if(!client){
			try{
				if(!BlockRegistry.blockIdsSetup){
					File blockIds = arg.getDataFile("blocks");
					if(blockIds.exists()){
						HashMap<Identifier, Integer> idMap = new HashMap<>();
						FileReader fr=new FileReader(blockIds);
						BufferedReader br=new BufferedReader(fr);
						StringBuffer sb=new StringBuffer();
						String line;
						int idIndex = 199;
						while((line=br.readLine())!=null)
						{
							idMap.put(new Identifier(line), idIndex);
							idIndex++;
						}
						fr.close();
						ContentRegistryImpl.registerBlocks(idMap);
					}else{
						//TODO: just pass to the registry to create new ids for the items/blocks. then get the id map back and put that in the file
					}

				}

				if(!ItemRegistry.itemIdsSetup){
					File itemIds = arg.getDataFile("items");
					if(itemIds.exists()){
						HashMap<Identifier, Integer> idMap = new HashMap<>();
						FileReader fr=new FileReader(itemIds);
						BufferedReader br=new BufferedReader(fr);
						StringBuffer sb=new StringBuffer();
						String line;
						int idIndex = 199;
						while((line=br.readLine())!=null)
						{
							idMap.put(new Identifier(line), idIndex);
							idIndex++;
						}
						fr.close();
						ContentRegistryImpl.registerItems(idMap);
					}else{
						//TODO: just pass to the registry to create new ids for the items/blocks. then get the id map back and put that in the file
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
