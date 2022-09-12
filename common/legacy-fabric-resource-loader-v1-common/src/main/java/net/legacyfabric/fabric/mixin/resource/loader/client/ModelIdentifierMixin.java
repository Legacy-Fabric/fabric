package net.legacyfabric.fabric.mixin.resource.loader.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ModelIdentifier.class)
public class ModelIdentifierMixin {
	@Inject(method = "split", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;indexOf(I)I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void modifyArray(String id, CallbackInfoReturnable<String[]> cir, String[] strings) {
		Identifier identifier = new Identifier((id));
		if (identifier.getNamespace().equals("minecraft")) {
			return;
		}
		strings[0] = identifier.getNamespace();
		strings[1] = identifier.getPath();
	}
}
