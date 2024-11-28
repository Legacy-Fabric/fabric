package net.legacyfabric.fabric.mixin.command;

import net.legacyfabric.fabric.impl.command.CrossCompatibleText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Text.class)
public abstract class TextMixin implements CrossCompatibleText {
	@Override
	public String asSanitizedString() {
		return this.toString();
	}
}
