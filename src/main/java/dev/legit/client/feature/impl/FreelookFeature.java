package dev.legit.client.feature.impl;

import dev.legit.client.Keys;
import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

public class FreelookFeature extends Feature {
	public float yaw;
	public float pitch;

	private boolean looking;
	private Perspective previous = Perspective.FIRST_PERSON;

	public FreelookFeature() {
		super("Freelook", "Hold the key to look around freely", Category.VISUAL);
	}

	public boolean isLooking() {
		return looking;
	}

	public void rotate(double dx, double dy) {
		yaw += (float) dx * 0.15f;
		pitch = Math.max(-90f, Math.min(90f, pitch + (float) dy * 0.15f));
	}

	@Override
	public void onTick(MinecraftClient mc) {
		boolean want = enabled
			&& Keys.freelookDown()
			&& mc.player != null
			&& mc.currentScreen == null;

		if (want && !looking) {
			looking = true;
			yaw = mc.player.getYaw();
			pitch = mc.player.getPitch();
			previous = mc.options.getPerspective();
			if (previous.isFirstPerson()) {
				mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
			}
		} else if (!want && looking) {
			looking = false;
			mc.options.setPerspective(previous);
		}
	}
}
