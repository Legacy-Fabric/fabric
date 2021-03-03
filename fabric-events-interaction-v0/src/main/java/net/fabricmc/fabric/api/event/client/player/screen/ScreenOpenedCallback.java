/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.event.client.player.screen;

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.util.ActionResult;

/**
 * This event is called before opening a {@link Screen}. It can be used to
 * modify the opened screen, as well as nullify it. Returning null
 * will cause the current screen to be closed. Returning null still may open
 * a screen, such as a {@link TitleScreen} when the player is not in any world.
 *
 * <p>Upon return:
 * <ul><li>CONSUME cancels further processing and does not modify the screen, thus opening the original screen.
 * <li>SUCCESS cancels further processing and opens the screen contained inside the AtomicReference.
 * <li>PASS falls back to further processing.
 * <li>FAIL cancels further processing and does not open the screen.</ul>
 * </p>
 */
@FunctionalInterface
public interface ScreenOpenedCallback {
	Event<ScreenOpenedCallback> EVENT = EventFactory.createArrayBacked(ScreenOpenedCallback.class, listeners -> screen -> {
		Screen original = screen.get();

		for (ScreenOpenedCallback callback : listeners) {
			ActionResult result = callback.onScreenOpened(screen);

			if (result != ActionResult.PASS) {
				return result;
			} else {
				screen.set(original);
			}
		}

		return ActionResult.PASS;
	});

	ActionResult onScreenOpened(AtomicReference<Screen> screen);
}
