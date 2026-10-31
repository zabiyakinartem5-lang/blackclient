package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import net.minecraft.client.MinecraftClient;

public class ToggleSprintFeature extends Feature {
	public ToggleSprintFeature() {
		super("ToggleSprint", "Sprint automatically while moving forward", Category.MOVEMENT);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || mc.player == null) return;
		if (mc.player.input.hasForwardMovement() && !mc.player.isSneaking()) {
			mc.options.sprintKey.setPressed(true);
		}
	}

	@Override
	public void onDisable() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc != null && mc.options != null) mc.options.sprintKey.setPressed(false);
	}
}
