package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;

public class WatermarkFeature extends Feature {
	public final BoolSetting showFps = add(new BoolSetting("Show FPS", true));
	public final BoolSetting showPing = add(new BoolSetting("Show ping", true));
	public final BoolSetting showClock = add(new BoolSetting("Show clock", true));

	public WatermarkFeature() {
		super("Watermark", "Client name with FPS, ping and clock", Category.HUD);
	}
}
