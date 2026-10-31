package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

/** Содержимое вашего инвентаря прямо на экране. */
public class InventoryHudFeature extends Feature {
	public final BoolSetting hotbar = add(new BoolSetting("Include hotbar", false));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public InventoryHudFeature() {
		super("Inventory HUD", "Your inventory on screen", Category.HUD);
	}
}
