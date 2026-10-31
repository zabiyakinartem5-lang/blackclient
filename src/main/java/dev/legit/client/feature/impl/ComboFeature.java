package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

public class ComboFeature extends Feature {
	public final BoolSetting reach = add(new BoolSetting("Show reach", true));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public ComboFeature() {
		super("Combo Counter", "Hit combo and distance of your last hit", Category.PVP);
	}
}
