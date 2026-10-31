package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** Показывает здоровье и броню последней цели, по которой вы били. */
public class TargetHudFeature extends Feature {
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public TargetHudFeature() {
		super("Target HUD", "Health and armor of your last target", Category.PVP);
	}
}
