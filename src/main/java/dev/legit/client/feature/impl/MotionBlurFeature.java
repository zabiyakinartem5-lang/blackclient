package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

public class MotionBlurFeature extends Feature {
	public final NumSetting strength = add(new NumSetting("Strength", 60, 10, 90, 5, "%"));

	public MotionBlurFeature() {
		super("Motion Blur", "Smooth trails between frames (experimental)", Category.VISUAL);
	}
}
