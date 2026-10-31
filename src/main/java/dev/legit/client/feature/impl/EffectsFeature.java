package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

public class EffectsFeature extends Feature {
	public final BoolSetting bar = add(new BoolSetting("Duration bar", true));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public EffectsFeature() {
		super("Potion HUD", "Active potion effects with timers and duration bars", Category.HUD);
	}
}
