package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

/** Счётчики расходников из инвентаря: тотемы, яблоки, жемчуг и т.д. */
public class PvpItemsFeature extends Feature {
	public final BoolSetting totems = add(new BoolSetting("Totems", true));
	public final BoolSetting gapples = add(new BoolSetting("Golden apples", true));
	public final BoolSetting pearls = add(new BoolSetting("Ender pearls", true));
	public final BoolSetting crystals = add(new BoolSetting("End crystals", false));
	public final BoolSetting potions = add(new BoolSetting("Splash potions", false));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public PvpItemsFeature() {
		super("PvP Items", "Counts of totems, apples, pearls in your inventory", Category.PVP);
	}
}
