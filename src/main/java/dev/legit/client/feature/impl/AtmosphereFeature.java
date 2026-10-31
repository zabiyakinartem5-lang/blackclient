package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** Лёгкий «шейдерный» фильтр поверх картинки: цветная тонировка и затемнение по краям (виньетка). */
public class AtmosphereFeature extends Feature {
	public final NumSetting hue = add(new NumSetting("Tint color", 270, 0, 360, 1, "°").asHue());
	public final NumSetting tint = add(new NumSetting("Tint strength", 12, 0, 60, 1, "%"));
	public final NumSetting vignette = add(new NumSetting("Vignette", 40, 0, 100, 5, "%"));

	public AtmosphereFeature() {
		super("Atmosphere", "Color tint and vignette over the screen", Category.VISUAL);
	}
}
