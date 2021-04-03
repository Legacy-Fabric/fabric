package net.legacyfabric.fabric.test.client;

import java.util.Arrays;

import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.resource.ResourceManagerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ClientModInitializer;

public class ResourceReloadTest implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitializeClient() {
		Identifier id = new Identifier("legacy-fabric-api", "test_reload");
		ResourceManagerHelper.getInstance().registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return id;
			}

			@Override
			public void reload(ResourceManager resourceManager) {
				LOGGER.info("Resources and reloading");
				LOGGER.info("Namespaces are {}", Arrays.toString(resourceManager.getAllNamespaces().toArray()));
			}
		});
	}
}
