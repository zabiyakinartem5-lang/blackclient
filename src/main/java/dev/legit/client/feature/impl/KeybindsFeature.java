package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BindSetting;
import org.lwjgl.glfw.GLFW;

/** Основные клавиши клиента. Клик по кнопке и нажатие клавиши назначает её, ПКМ или Backspace сбрасывает. */
public class KeybindsFeature extends Feature {
	public final BindSetting openMenu = add(new BindSetting("Open menu", GLFW.GLFW_KEY_RIGHT_SHIFT));
	public final BindSetting zoom = add(new BindSetting("Zoom (hold)", GLFW.GLFW_KEY_C));
	public final BindSetting freelook = add(new BindSetting("Freelook (hold)", GLFW.GLFW_KEY_LEFT_ALT));
	public final BindSetting mark = add(new BindSetting("Add mark here", GLFW.GLFW_KEY_B));

	public KeybindsFeature() {
		super("Keybinds", "Assign keys inside the client", Category.CLIENT);
		noBind();
		this.enabled = true;
		this.anim = 1f;
		this.alwaysOn = true;
	}
}
