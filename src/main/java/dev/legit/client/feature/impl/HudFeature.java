package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

public class HudFeature extends Feature {
	public final BoolSetting fps = add(new BoolSetting("FPS", true));
	public final BoolSetting coords = add(new BoolSetting("Coordinates", true));
	public final BoolSetting cps = add(new BoolSetting("CPS", true));
	public final BoolSetting biome = add(new BoolSetting("Biome", false));
	public final BoolSetting ping = add(new BoolSetting("Ping", true));
	public final BoolSetting speed = add(new BoolSetting("Speed", false));
	public final BoolSetting direction = add(new BoolSetting("Direction", false));
	public final BoolSetting clock = add(new BoolSetting("Clock", false));
	public final BoolSetting server = add(new BoolSetting("Server IP", false));
	public final BoolSetting day = add(new BoolSetting("Day counter", false));
	public final BoolSetting ram = add(new BoolSetting("RAM", false));
	public final BoolSetting armor = add(new BoolSetting("Armor", true));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public HudFeature() {
		super("HUD", "Info overlay and armor", Category.HUD);
		this.enabled = true;
		this.anim = 1f;
	}
}
