package dev.legit.client;

import dev.legit.client.feature.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;
import java.util.Map;

/** Опрос клавиш. Сами клавиши назначаются в меню: вкладка Client → Keybinds и строка Bind у каждого модуля. */
public final class Keys {
	private static final Map<Integer, Boolean> PREV = new HashMap<>();

	private Keys() {}

	public static boolean down(int key) {
		if (key <= 0) return false;
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc == null || mc.getWindow() == null) return false;
		return InputUtil.isKeyPressed(mc.getWindow().getHandle(), key);
	}

	/** true один раз в момент нажатия. Вызывать не чаще одного раза за тик для одной клавиши. */
	public static boolean pressedOnce(int key) {
		if (key <= 0) return false;
		boolean now = down(key);
		boolean was = PREV.getOrDefault(key, false);
		PREV.put(key, now);
		return now && !was;
	}

	public static int gui() {
		return Features.KEYBINDS.openMenu.key;
	}

	public static int mark() {
		return Features.KEYBINDS.mark.key;
	}

	private static boolean inWorld() {
		MinecraftClient mc = MinecraftClient.getInstance();
		return mc != null && mc.currentScreen == null;
	}

	public static boolean zoomDown() {
		return inWorld() && down(Features.KEYBINDS.zoom.key);
	}

	public static boolean freelookDown() {
		return inWorld() && down(Features.KEYBINDS.freelook.key);
	}
}
