package net.legacyfabric.fabric.impl.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.legacyfabric.fabric.api.event.Event;
import net.legacyfabric.fabric.api.event.EventFactory;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class ScreenEventFactory {
	public static Event<ScreenEvents.Remove> createRemoveEvent() {
		return EventFactory.createArrayBacked(ScreenEvents.Remove.class, callbacks -> screen -> {
			for (ScreenEvents.Remove callback : callbacks) {
				callback.onRemove(screen);
			}
		});
	}
	
	public static Event<ScreenEvents.BeforeRender> createBeforeRenderEvent() {
		return EventFactory.createArrayBacked(ScreenEvents.BeforeRender.class, callbacks ->
				(screen, mouseX, mouseY, tickDelta) -> {
			for (ScreenEvents.BeforeRender callback : callbacks) {
				callback.beforeRender(screen, mouseX, mouseY, tickDelta);
			}
		});
	}
	
	public static Event<ScreenEvents.AfterRender> createAfterRenderEvent() {
		return EventFactory.createArrayBacked(ScreenEvents.AfterRender.class, callbacks ->
				(screen, mouseX, mouseY, tickDelta) -> {
			for (ScreenEvents.AfterRender callback : callbacks) {
				callback.afterRender(screen, mouseX, mouseY, tickDelta);
			}
		});
	}
	
	public static Event<ScreenEvents.BeforeTick> createBeforeTickEvent() {
		return EventFactory.createArrayBacked(ScreenEvents.BeforeTick.class, callbacks -> screen -> {
			for (ScreenEvents.BeforeTick callback : callbacks) {
				callback.beforeTick(screen);
			}
		});
	}
	
	public static Event<ScreenEvents.AfterTick> createAfterTickEvent() {
		return EventFactory.createArrayBacked(ScreenEvents.AfterTick.class, callbacks -> screen -> {
			for (ScreenEvents.AfterTick callback : callbacks) {
				callback.afterTick(screen);
			}
		});
	}
	
	// Keyboard events
	
	public static Event<ScreenKeyboardEvents.AllowKeyPress> createAllowKeyPressEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyPress.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.AllowKeyPress callback : callbacks) {
				if (!callback.allowKeyPress(screen, key)) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	public static Event<ScreenKeyboardEvents.BeforeKeyPress> createBeforeKeyPressEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyPress.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.BeforeKeyPress callback : callbacks) {
				callback.beforeKeyPress(screen, key);
			}
		});
	}
	
	public static Event<ScreenKeyboardEvents.AfterKeyPress> createAfterKeyPressEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyPress.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.AfterKeyPress callback : callbacks) {
				callback.afterKeyPress(screen, key);
			}
		});
	}
	
	public static Event<ScreenKeyboardEvents.AllowKeyRelease> createAllowKeyReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyRelease.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.AllowKeyRelease callback : callbacks) {
				if (!callback.allowKeyRelease(screen, key)) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	public static Event<ScreenKeyboardEvents.BeforeKeyRelease> createBeforeKeyReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyRelease.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.BeforeKeyRelease callback : callbacks) {
				callback.beforeKeyRelease(screen, key);
			}
		});
	}
	
	public static Event<ScreenKeyboardEvents.AfterKeyRelease> createAfterKeyReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyRelease.class, callbacks ->
				(screen, key) -> {
			for (ScreenKeyboardEvents.AfterKeyRelease callback : callbacks) {
				callback.afterKeyRelease(screen, key);
			}
		});
	}
	
	// Mouse Events
	
	public static Event<ScreenMouseEvents.AllowMouseClick> createAllowMouseClickEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseClick.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.AllowMouseClick callback : callbacks) {
				if (!callback.allowMouseClick(screen, mouseX, mouseY, button)) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	public static Event<ScreenMouseEvents.BeforeMouseClick> createBeforeMouseClickEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseClick.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.BeforeMouseClick callback : callbacks) {
				callback.beforeMouseClick(screen, mouseX, mouseY, button);
			}
		});
	}
	
	public static Event<ScreenMouseEvents.AfterMouseClick> createAfterMouseClickEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseClick.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.AfterMouseClick callback : callbacks) {
				callback.afterMouseClick(screen, mouseX, mouseY, button);
			}
		});
	}
	
	public static Event<ScreenMouseEvents.AllowMouseRelease> createAllowMouseReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseRelease.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.AllowMouseRelease callback : callbacks) {
				if (!callback.allowMouseRelease(screen, mouseX, mouseY, button)) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	public static Event<ScreenMouseEvents.BeforeMouseRelease> createBeforeMouseReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseRelease.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.BeforeMouseRelease callback : callbacks) {
				callback.beforeMouseRelease(screen, mouseX, mouseY, button);
			}
		});
	}
	
	public static Event<ScreenMouseEvents.AfterMouseRelease> createAfterMouseReleaseEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseRelease.class, callbacks ->
				(screen, mouseX, mouseY, button) -> {
			for (ScreenMouseEvents.AfterMouseRelease callback : callbacks) {
				callback.afterMouseRelease(screen, mouseX, mouseY, button);
			}
		});
	}
	
	public static Event<ScreenMouseEvents.AllowMouseScroll> createAllowMouseScrollEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseScroll.class, callbacks ->
				(screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (ScreenMouseEvents.AllowMouseScroll callback : callbacks) {
				if (!callback.allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	public static Event<ScreenMouseEvents.BeforeMouseScroll> createBeforeMouseScrollEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseScroll.class, callbacks ->
				(screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (ScreenMouseEvents.BeforeMouseScroll callback : callbacks) {
				callback.beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
	}
	
	public static Event<ScreenMouseEvents.AfterMouseScroll> createAfterMouseScrollEvent() {
		return EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseScroll.class, callbacks ->
				(screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (ScreenMouseEvents.AfterMouseScroll callback : callbacks) {
				callback.afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
	}
	
	private ScreenEventFactory() {}
}
