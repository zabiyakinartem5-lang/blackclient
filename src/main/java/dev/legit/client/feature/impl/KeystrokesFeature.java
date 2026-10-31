package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

public class KeystrokesFeature extends Feature {
	public final BoolSetting mouse = add(new BoolSetting("Mouse buttons", true));
	public final BoolSetting space = add(new BoolSetting("Space bar", true));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public KeystrokesFeature() {
		super("Keystrokes", "Show pressed keys and mouse buttons", Category.HUD);
	}
}
