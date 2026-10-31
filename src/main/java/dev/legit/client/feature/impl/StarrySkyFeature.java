package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import net.minecraft.client.MinecraftClient;

/** Звёздное небо: вечная ночь и яркие звёзды. Меняется только картинка у вас на экране, сервер не затрагивается. */
public class StarrySkyFeature extends Feature {
	public final BoolSetting forceNight = add(new BoolSetting("Force night", true));
	public final BoolSetting brightStars = add(new BoolSetting("Bright stars", true));

	public StarrySkyFeature() {
		super("Starry Sky", "Always night with bright stars (client side)", Category.VISUAL);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || !forceNight.value || mc.world == null) return;
		mc.world.getLevelProperties().setTimeOfDay(18000L);
	}
}
