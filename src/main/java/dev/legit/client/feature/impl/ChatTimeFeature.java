package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;

public class ChatTimeFeature extends Feature {
	public final BoolSetting seconds = add(new BoolSetting("Show seconds", false));

	public ChatTimeFeature() {
		super("Chat Time", "Timestamp before every chat message", Category.HUD);
	}
}
