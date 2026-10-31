package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;

/** Настройки собственного главного меню. Цвета, прозрачность и скругление берутся из Client → Theme. */
public class MainMenuFeature extends Feature {
	public final BoolSetting alignRight = add(new BoolSetting("Align right", true));
	public final BoolSetting orbs = add(new BoolSetting("Glow orbs", true));
	public final BoolSetting stars = add(new BoolSetting("Starry background", true));
	public final BoolSetting playerCard = add(new BoolSetting("Player card", true));
	public final BoolSetting clock = add(new BoolSetting("Clock", true));
	public final NumSetting darkness = add(new NumSetting("Darkness", 55, 0, 90, 5, "%"));
	public final NumSetting orbSpeed = add(new NumSetting("Orb speed", 3, 1, 10, 1, ""));
	public final NumSetting width = add(new NumSetting("Menu width", 210, 150, 280, 10, ""));
	public final NumSetting buttonHeight = add(new NumSetting("Button height", 24, 18, 34, 1, ""));

	public MainMenuFeature() {
		super("Main Menu", "Custom title screen (disable to get vanilla)", Category.CLIENT);
		noBind();
		this.enabled = true;
		this.anim = 1f;
	}
}
