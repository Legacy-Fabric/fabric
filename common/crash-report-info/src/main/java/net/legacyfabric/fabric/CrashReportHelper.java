package net.legacyfabric.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Map;
import java.util.TreeMap;

public class CrashReportHelper {

	public static String getFabricMods() {
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
	}
}
