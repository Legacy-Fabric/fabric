package net.legacyfabric.fabric.mixin.crashreport;

import java.util.Map;
import java.util.TreeMap;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

@Mixin(CrashReport.class)
public class CrashReportMixin {
	@Shadow
	@Final
	private CrashReportSection systemDetailsSection;

	@Inject(at = @At("RETURN"), method = "method_27628")
	private void fillSystemDetails(CallbackInfo info) {
		this.systemDetailsSection.add("Fabric Mods", () -> {
			Map<String, String> mods = new TreeMap<>();

			for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
				mods.put(container.getMetadata().getId(), container.getMetadata().getName() + " " + container.getMetadata().getVersion().getFriendlyString());
			}

			StringBuilder modString = new StringBuilder();

			for (String id : mods.keySet()) {
				modString.append("\n\t\t");
				modString.append(id);
				modString.append(": ");
				modString.append(mods.get(id));
			}

			return modString.toString();
		});
	}
}
