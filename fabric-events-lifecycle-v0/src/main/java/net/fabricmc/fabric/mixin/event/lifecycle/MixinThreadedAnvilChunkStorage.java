package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.world.ChunksSavedCallback;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public class MixinThreadedAnvilChunkStorage {

	@Inject(at=@At(value = "INVOKE",target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;[Ljava/lang/Object;)V"),method = "shouldRenderOverlay")
	public void shouldRenderOverlay(CallbackInfoReturnable<Boolean> info){
		ChunksSavedCallback.EVENT.invoker().chunksSaved();
	}

}
