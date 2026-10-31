package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import net.minecraft.client.MinecraftClient;

public class ToggleSneakFeature extends Feature {
	private boolean latched;

	public ToggleSneakFeature() {
		super("ToggleSneak", "Press sneak once to stay sneaking", Category.MOVEMENT);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || mc.player == null) return;
		if (mc.currentScreen != null) return;

		// каждое нажатие клавиши приседа переключает защёлку
		while (mc.options.sneakKey.wasPressed()) {
			latched = !latched;
		}
		mc.options.sneakKey.setPressed(latched);
	}

	@Override
	public void onDisable() {
		latched = false;
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc != null && mc.options != null) {
			while (mc.options.sneakKey.wasPressed()) { /* сбросить счётчик нажатий */ }
			mc.options.sneakKey.setPressed(false);
		}
	}
}
