package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** Небольшая плашка: Walking / Sprinting / Sneaking. */
public class StatusFeature extends Feature {
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public StatusFeature() {
		super("Move Status", "Shows if you are walking, sprinting or sneaking", Category.HUD);
	}
}
