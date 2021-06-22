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

package net.legacyfabric.fabric.mixin.screen;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import net.legacyfabric.fabric.api.client.screen.v1.ScreenEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.impl.client.screen.ScreenEventFactory;
import net.legacyfabric.fabric.impl.client.screen.ScreenExtensions;

@Mixin(Screen.class)
abstract class ScreenMixin implements ScreenExtensions {
	@Unique
	private AbstractList<ButtonWidget> fabricButtons;
	@Unique
	private Event<ScreenEvents.Remove> removeEvent;
	@Unique
	private Event<ScreenEvents.BeforeTick> beforeTickEvent;
	@Unique
	private Event<ScreenEvents.AfterTick> afterTickEvent;
	@Unique
	private Event<ScreenEvents.BeforeRender> beforeRenderEvent;
	@Unique
	private Event<ScreenEvents.AfterRender> afterRenderEvent;

	// Keyboard
	@Unique
	private Event<ScreenKeyboardEvents.AllowKeyPress> allowKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.BeforeKeyPress> beforeKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AfterKeyPress> afterKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AllowKeyRelease> allowKeyReleaseEvent;
	@Unique
	private Event<ScreenKeyboardEvents.BeforeKeyRelease> beforeKeyReleaseEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AfterKeyRelease> afterKeyReleaseEvent;

	// Mouse
	@Unique
	private Event<ScreenMouseEvents.AllowMouseClick> allowMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseClick> beforeMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseClick> afterMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.AllowMouseRelease> allowMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseRelease> beforeMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseRelease> afterMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.AllowMouseDrag> allowMouseDragEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseDrag> beforeMouseDragEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseDrag> afterMouseDragEvent;

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("HEAD"))
	private void beforeInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		// All elements are repopulated on the screen, so we need to reinitialize all events
		this.fabricButtons = null;
		this.removeEvent = ScreenEventFactory.createRemoveEvent();
		this.beforeRenderEvent = ScreenEventFactory.createBeforeRenderEvent();
		this.afterRenderEvent = ScreenEventFactory.createAfterRenderEvent();
		this.beforeTickEvent = ScreenEventFactory.createBeforeTickEvent();
		this.afterTickEvent = ScreenEventFactory.createAfterTickEvent();

		// Keyboard
		this.allowKeyPressEvent = ScreenEventFactory.createAllowKeyPressEvent();
		this.beforeKeyPressEvent = ScreenEventFactory.createBeforeKeyPressEvent();
		this.afterKeyPressEvent = ScreenEventFactory.createAfterKeyPressEvent();
		this.allowKeyReleaseEvent = ScreenEventFactory.createAllowKeyReleaseEvent();
		this.beforeKeyReleaseEvent = ScreenEventFactory.createBeforeKeyReleaseEvent();
		this.afterKeyReleaseEvent = ScreenEventFactory.createAfterKeyReleaseEvent();

		// Mouse
		this.allowMouseClickEvent = ScreenEventFactory.createAllowMouseClickEvent();
		this.beforeMouseClickEvent = ScreenEventFactory.createBeforeMouseClickEvent();
		this.afterMouseClickEvent = ScreenEventFactory.createAfterMouseClickEvent();
		this.allowMouseReleaseEvent = ScreenEventFactory.createAllowMouseReleaseEvent();
		this.beforeMouseReleaseEvent = ScreenEventFactory.createBeforeMouseReleaseEvent();
		this.afterMouseReleaseEvent = ScreenEventFactory.createAfterMouseReleaseEvent();
		this.allowMouseDragEvent = ScreenEventFactory.createAllowMouseScrollEvent();
		this.beforeMouseDragEvent = ScreenEventFactory.createBeforeMouseScrollEvent();
		this.afterMouseDragEvent = ScreenEventFactory.createAfterMouseScrollEvent();

		ScreenEvents.BEFORE_INIT.invoker().beforeInit(client, (Screen) (Object) this, width, height);
	}

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
	private void afterInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		ScreenEvents.AFTER_INIT.invoker().afterInit(client, (Screen) (Object) this, width, height);
	}

	@Override
	public List<ButtonWidget> fabric_getButtons() {
		// Lazy init to make the list access safe after Screen#init
		if (this.fabricButtons == null) {
			this.fabricButtons = new ArrayList<>();
		}

		return this.fabricButtons;
	}

	@Unique
	private <T> Event<T> ensureEventsAreInitialised(Event<T> event) {
		if (event == null) {
			throw new IllegalStateException(String.format("[fabric-screen-api-v1] The current screen (%s) has not been correctly initialised, please send this crash log to the mod author. This is usually caused by the screen not calling super.init(Lnet/minecraft/client/MinecraftClient;II)V", this.getClass().getSuperclass().getName()));
		}

		return event;
	}

	@Inject(method = "mouseClicked(III)V", at = @At(value = "HEAD"), cancellable = true)
	private void beforeMouseClickedEvent(CallbackInfo ci) {
		Screen thisRef = (Screen) (Object) this;
		MinecraftClient client = MinecraftClient.getInstance();
		int i = Mouse.getEventX() * thisRef.width / client.width;
		int j = thisRef.height - Mouse.getEventY() * thisRef.height / client.height - 1;
		int k = Mouse.getEventButton();

		if (!ScreenMouseEvents.allowMouseClick(thisRef).invoker().allowMouseClick(thisRef, i, j, k)) {
			ci.cancel();
			return;
		}

		ScreenMouseEvents.beforeMouseClick(thisRef).invoker().beforeMouseClick(thisRef, i, j, k);
	}

	@Inject(method = "mouseClicked", at = @At("TAIL"))
	private void afterMouseClickedEvent(CallbackInfo ci) {
		Screen thisRef = (Screen) (Object) this;
		MinecraftClient client = MinecraftClient.getInstance();
		int i = Mouse.getEventX() * thisRef.width / client.width;
		int j = thisRef.height - Mouse.getEventY() * thisRef.height / client.height - 1;
		int k = Mouse.getEventButton();

		ScreenMouseEvents.afterMouseClick(thisRef).invoker().afterMouseClick(thisRef, i, j, k);
	}

	@Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
	private void beforeMouseReleasedEvent(int x, int y, int button, CallbackInfo ci) {
		Screen thisRef = (Screen) (Object) this;

		if (!ScreenMouseEvents.allowMouseRelease(thisRef).invoker().allowMouseRelease(thisRef, x, y, button)) {
			ci.cancel();
			return;
		}

		ScreenMouseEvents.beforeMouseRelease(thisRef).invoker().beforeMouseRelease(thisRef, x, y, button);
	}

	@Inject(method = "mouseReleased", at = @At("TAIL"))
	private void afterMouseReleaseEvent(int x, int y, int button, CallbackInfo ci) {
		Screen thisRef = (Screen) (Object) this;

		ScreenMouseEvents.afterMouseRelease(thisRef).invoker().afterMouseRelease(thisRef, x, y, button);
	}

	@Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
	private void beforeMouseDragEvent(int x, int y, int mouseButton, long duration, CallbackInfo ci) {
		Screen thisRef = (Screen) (Object) this;

		if (!ScreenMouseEvents.allowMouseDrag(thisRef).invoker().allowMouseDrag(thisRef, x, y, mouseButton, duration)) {
			ci.cancel();
			return;
		}

		ScreenMouseEvents.beforeMouseDrag(thisRef).invoker().beforeMouseDrag(thisRef, x, y, mouseButton, duration);
	}

	@Override
	public Event<ScreenEvents.Remove> fabric_getRemoveEvent() {
		return ensureEventsAreInitialised(this.removeEvent);
	}

	@Override
	public Event<ScreenEvents.BeforeTick> fabric_getBeforeTickEvent() {
		return ensureEventsAreInitialised(this.beforeTickEvent);
	}

	@Override
	public Event<ScreenEvents.AfterTick> fabric_getAfterTickEvent() {
		return ensureEventsAreInitialised(this.afterTickEvent);
	}

	@Override
	public Event<ScreenEvents.BeforeRender> fabric_getBeforeRenderEvent() {
		return ensureEventsAreInitialised(this.beforeRenderEvent);
	}

	@Override
	public Event<ScreenEvents.AfterRender> fabric_getAfterRenderEvent() {
		return ensureEventsAreInitialised(this.afterRenderEvent);
	}

	// Keyboard

	@Override
	public Event<ScreenKeyboardEvents.AllowKeyPress> fabric_getAllowKeyPressEvent() {
		return ensureEventsAreInitialised(this.allowKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.BeforeKeyPress> fabric_getBeforeKeyPressEvent() {
		return ensureEventsAreInitialised(this.beforeKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AfterKeyPress> fabric_getAfterKeyPressEvent() {
		return ensureEventsAreInitialised(this.afterKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AllowKeyRelease> fabric_getAllowKeyReleaseEvent() {
		return ensureEventsAreInitialised(this.allowKeyReleaseEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.BeforeKeyRelease> fabric_getBeforeKeyReleaseEvent() {
		return ensureEventsAreInitialised(this.beforeKeyReleaseEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AfterKeyRelease> fabric_getAfterKeyReleaseEvent() {
		return ensureEventsAreInitialised(this.afterKeyReleaseEvent);
	}

	// Mouse

	@Override
	public Event<ScreenMouseEvents.AllowMouseClick> fabric_getAllowMouseClickEvent() {
		return ensureEventsAreInitialised(this.allowMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseClick> fabric_getBeforeMouseClickEvent() {
		return ensureEventsAreInitialised(this.beforeMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseClick> fabric_getAfterMouseClickEvent() {
		return ensureEventsAreInitialised(this.afterMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AllowMouseRelease> fabric_getAllowMouseReleaseEvent() {
		return ensureEventsAreInitialised(this.allowMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseRelease> fabric_getBeforeMouseReleaseEvent() {
		return ensureEventsAreInitialised(this.beforeMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseRelease> fabric_getAfterMouseReleaseEvent() {
		return ensureEventsAreInitialised(this.afterMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AllowMouseDrag> fabric_getAllowMouseDragEvent() {
		return ensureEventsAreInitialised(this.allowMouseDragEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseDrag> fabric_getBeforeMouseDragEvent() {
		return ensureEventsAreInitialised(this.beforeMouseDragEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseDrag> fabric_getAfterMouseDragEvent() {
		return ensureEventsAreInitialised(this.afterMouseDragEvent);
	}
}
