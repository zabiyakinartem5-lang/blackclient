package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** Добавляет смещение к текущему FOV (можно выйти за пределы ползунка из настроек игры). */
public class FovFeature extends Feature {
	public final NumSetting offset = add(new NumSetting("Offset", 10, -40, 60, 1, "°"));

	public FovFeature() {
		super("FOV Offset", "Add or remove degrees from your field of view", Category.VISUAL);
	}
}
