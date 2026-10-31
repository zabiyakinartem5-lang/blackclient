package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** Список включённых модулей в правом верхнем углу. */
public class ModuleListFeature extends Feature {
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public ModuleListFeature() {
		super("Module List", "Shows enabled modules on screen", Category.HUD);
	}
}
