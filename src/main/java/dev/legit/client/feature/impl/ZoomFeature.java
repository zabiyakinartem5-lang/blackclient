package dev.legit.client.feature.impl;

import dev.legit.client.Keys;
import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

public class ZoomFeature extends Feature {
	public final NumSetting factor = add(new NumSetting("Zoom", 4.0, 1.5, 10.0, 0.5, "x"));

	private float current = 1f;
	private long last = System.nanoTime();

	public ZoomFeature() {
		super("Zoom", "Hold the zoom key, scroll to adjust", Category.VISUAL);
	}

	public boolean active() {
		return enabled && Keys.zoomDown();
	}

	/** Вызывается каждый кадр: плавно приближаем текущее значение к целевому. */
	public void update() {
		long now = System.nanoTime();
		float dt = Math.min(0.1f, (now - last) / 1_000_000_000f);
		last = now;
		float target = active() ? (float) factor.value : 1f;
		current += (target - current) * Math.min(1f, dt * 14f);
		if (Math.abs(target - current) < 0.001f) current = target;
	}

	public float currentFactor() {
		return current;
	}

	public void scroll(double amount) {
		factor.set(factor.value + Math.signum(amount) * 0.5);
	}
}
