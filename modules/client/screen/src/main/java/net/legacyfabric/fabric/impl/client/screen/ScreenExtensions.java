package net.legacyfabric.fabric.impl.client.screen;

import net.legacyfabric.fabric.api.client.screen.v1.ScreenEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.legacyfabric.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.legacyfabric.fabric.api.event.Event;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.List;

public interface ScreenExtensions {
	static  ScreenExtensions getExtensions(Screen screen) {
		return (ScreenExtensions) screen;
	}
	
	List<ButtonWidget> fabric_getButtons();
	
	Event<ScreenEvents.Remove> fabric_getRemoveEvent();
	
	Event<ScreenEvents.BeforeTick> fabric_getBeforeTickEvent();
	
	Event<ScreenEvents.AfterTick> fabric_getAfterTickEvent();
	
	Event<ScreenEvents.BeforeRender> fabric_getBeforeRenderEvent();
	
	Event<ScreenEvents.AfterRender> fabric_getAfterRenderEvent();
	
	// Keyboard
	
	Event<ScreenKeyboardEvents.AllowKeyPress> fabric_getAllowKeyPressEvent();
	
	Event<ScreenKeyboardEvents.BeforeKeyPress> fabric_getBeforeKeyPressEvent();
	
	Event<ScreenKeyboardEvents.AfterKeyPress> fabric_getAfterKeyPressEvent();
	
	Event<ScreenKeyboardEvents.AllowKeyRelease> fabric_getAllowKeyReleaseEvent();
	
	Event<ScreenKeyboardEvents.BeforeKeyRelease> fabric_getBeforeKeyReleaseEvent();
	
	Event<ScreenKeyboardEvents.AfterKeyRelease> fabric_getAfterKeyReleaseEvent();
	
	// Mouse
	
	Event<ScreenMouseEvents.AllowMouseClick> fabric_getAllowMouseClickEvent();
	
	Event<ScreenMouseEvents.BeforeMouseClick> fabric_getBeforeMouseClickEvent();
	
	Event<ScreenMouseEvents.AfterMouseClick> fabric_getAfterMouseClickEvent();
	
	Event<ScreenMouseEvents.AllowMouseRelease> fabric_getAllowMouseReleaseEvent();
	
	Event<ScreenMouseEvents.BeforeMouseRelease> fabric_getBeforeMouseReleaseEvent();
	
	Event<ScreenMouseEvents.AfterMouseRelease> fabric_getAfterMouseReleaseEvent();
	
	Event<ScreenMouseEvents.AllowMouseScroll> fabric_getAllowMouseScrollEvent();
	
	Event<ScreenMouseEvents.BeforeMouseScroll> fabric_getBeforeMouseScrollEvent();
	
	Event<ScreenMouseEvents.AfterMouseScroll> fabric_getAfterMouseScrollEvent();
}
