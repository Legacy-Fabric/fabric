package net.fabricmc.fabric.mixin.resource.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Language;

import net.fabricmc.loader.launch.knot.Knot;

@Mixin(Language.class)
public class MixinLanguage {
	@Shadow
	@Final
	private Map<String, String> translations;

	@Shadow
	@Final
	private static Pattern field_5901;

	@Shadow
	@Final
	private static Splitter SPLITTER;

	/** @reason adding translations from mods */
	@Inject(method = "<init>", at = @At("RETURN"))
	public void addModdedTranslations(CallbackInfo ci) {
		// we don't have to check for translations inside resource packs
		// because this runs on both sides
		try {
			Enumeration<URL> urls = Knot.getLauncher().getTargetClassLoader().getResources("/assets/minecraft/lang/en_US.lang");
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				for (String string : IOUtils.readLines(url.openStream(), Charsets.UTF_8)) {
					if (!string.isEmpty() && string.charAt(0) != '#') {
						String[] strings = Iterables.toArray(SPLITTER.split(string), String.class);
						if (strings != null && strings.length == 2) {
							String string2 = strings[0];
							String string3 = field_5901.matcher(strings[1]).replaceAll("%$1s");
							this.translations.put(string2, string3);
						}
					}
				}
			}
		} catch (IOException ignored) {
			// minecraft eats the exception too...
		}
	}
}
