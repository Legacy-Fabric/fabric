package net.legacyfabric.fabric.mixin.resource.loader.client;

import java.util.List;

import net.legacyfabric.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReloadListener;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin {
	@Shadow
	@Final
	private List<ResourceReloadListener> listeners;

	@Inject(method = "reload", at = @At(value = "INVOKE", remap = false, target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V"))
	public void onReload(List<ResourcePack> resourcePacks, CallbackInfo ci) {
		ResourceManagerHelperImpl.getInstance().sort(this.listeners);
	}
}
