package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

/** Свой прицел вместо ванильного. */
public class CrosshairFeature extends Feature {
	public final NumSetting gap = add(new NumSetting("Gap", 3, 0, 10, 1, ""));
	public final NumSetting length = add(new NumSetting("Length", 5, 1, 14, 1, ""));
	public final NumSetting thickness = add(new NumSetting("Thickness", 1, 1, 4, 1, ""));
	public final BoolSetting dot = add(new BoolSetting("Center dot", false));
	public final BoolSetting outline = add(new BoolSetting("Outline", true));
	public final BoolSetting themeColor = add(new BoolSetting("Theme color", true));

	public CrosshairFeature() {
		super("Crosshair", "Custom crosshair", Category.VISUAL);
	}
}
