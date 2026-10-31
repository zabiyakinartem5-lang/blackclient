package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;

/** Меняет время суток только на вашем экране. Сервер и другие игроки ничего не замечают. */
public class TimeChangerFeature extends Feature {
	public final NumSetting time = add(new NumSetting("Time", 6000, 0, 24000, 500, ""));

	public TimeChangerFeature() {
		super("Time Changer", "Client-side time of day (0 dawn, 6000 noon, 18000 night)", Category.VISUAL);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || mc.world == null) return;
		mc.world.getLevelProperties().setTimeOfDay((long) time.value);
	}
}
