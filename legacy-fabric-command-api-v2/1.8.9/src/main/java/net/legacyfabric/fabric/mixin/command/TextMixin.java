package net.legacyfabric.fabric.mixin.command;

import net.legacyfabric.fabric.impl.command.CrossCompatibleText;

import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Text.class)
public abstract class TextMixin implements CrossCompatibleText {

	@Shadow
	public abstract String asUnformattedString();

	@Override
	public String asSanitizedString() {
		return this.asUnformattedString();
	}
}
