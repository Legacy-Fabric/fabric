package net.legacyfabric.fabric.commands.mixin;

import com.mojang.brigadier.Message;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Text.class)
public abstract interface TextMixin extends Message {

	@Shadow
	public abstract String asString();

	@Override
	default String getString() {
		return asString();
	}
}
