package net.legacyfabric.fabric.test.client;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.client.screen.v1.ScreenEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.legacyfabric.fabric.api.client.screen.v1.Screens;
import net.legacyfabric.fabric.mixin.screen.ScreenAccessor;

@Environment(EnvType.CLIENT)
public class ScreenTest implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("LegacyFabricScreenApiTests");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Started Screen Testmod");
		ScreenEvents.BEFORE_INIT.register((client, screen, width, height) -> {
			// TODO: Write tests listening to addition of child elements
		});

		ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
	}

	private void afterInitScreen(MinecraftClient client, Screen screen, int windowWidth, int windowHeight) {
		LOGGER.info("Initializing {}", screen.getClass().getName());

		if (screen instanceof TitleScreen) {
			final List<ButtonWidget> buttons = Screens.getButtons(screen);

			// Shrink the realms button, should be the third button on the list
			final ButtonWidget optionsButton = buttons.get(2);
			optionsButton.setWidth(98);

			// Add a new button
			buttons.add(new SoundButton(1, (screen.width / 2) + 2, ((screen.height / 4) + 96), 72, 20));
			// And another button
			buttons.add(new StopSoundButton(screen, 2, (screen.width / 2) + 80, ((screen.height / 4) + 95), 20, 20));

			// Testing:
			// Some automatic validation that the screen list works, make sure the buttons we added are on the list of child elements
			((ScreenAccessor) screen).getButtons().stream()
					.filter(button -> button instanceof SoundButton)
					.findAny()
					.orElseThrow(() -> new AssertionError("Failed to find the \"Sound\" button in the screen's elements"));

			((ScreenAccessor) screen).getButtons().stream()
					.filter(button -> button instanceof StopSoundButton)
					.findAny()
					.orElseThrow(() -> new AssertionError("Failed to find the \"Stop Sound\" button in the screen's elements"));

			// Register render event to draw an icon on the screen
			ScreenEvents.afterRender(screen).register((_screen, mouseX, mouseY, tickDelta) -> {
				// Render an armor icon to test
				Screen.drawTexture((screen.width / 2) - 124, (screen.height / 4) + 96, 20, 20, 34, 9, 9, 9, 256, 256);
			});

			ScreenKeyboardEvents.allowKeyPress(screen).register((_screen, key) -> {
				LOGGER.info("After Pressed, Code: {}, Scancode: {}", _screen, key);
				return true; // Let actions continue
			});

			ScreenKeyboardEvents.afterKeyPress(screen).register((screen1, key) -> {
				LOGGER.warn("Pressed, Code: {}, Scancode: {}", screen1, key);
			});
		}
	}

	private static class SoundButton extends ButtonWidget {
		private static final Random RANDOM = new Random();

		SoundButton(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height, "Sound Button");
		}

		@Override
		public boolean onClick(MinecraftClient client, int mouseX, int mouseY) {
			// Upcast on registry is fine
			@Nullable
			final SoundEvent event = SoundEvent.REGISTRY.getRandom(RANDOM);

			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(event != null ? event : SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f));
			return super.onClick(client, mouseX, mouseY);
		}
	}

	private static class StopSoundButton extends ButtonWidget {
		private final Screen screen;

		StopSoundButton(Screen screen, int id, int x, int y, int width, int height) {
			super(id, x, y, width, height, "");
			this.screen = screen;
		}

		@Override
		protected void render(MinecraftClient client, int mouseX, int mouseY) {
			// Render an armor icon to test
			Screen.drawTexture(this.x, this.y, this.width, this.height, 43, 27, 9, 9, 256, 256);

			if (this.isHovered()) {
				this.screen.renderTooltip("Click to stop all sounds", this.x, this.y);
			}
		}

		@Override
		public boolean onClick(MinecraftClient client, int mouseX, int mouseY) {
			MinecraftClient.getInstance().getSoundManager().stopAll();
			return super.onClick(client, mouseX, mouseY);
		}
	}
}
