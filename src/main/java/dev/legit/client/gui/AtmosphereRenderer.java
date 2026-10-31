package dev.legit.client.gui;

import dev.legit.client.feature.Features;
import dev.legit.client.feature.impl.AtmosphereFeature;
import net.minecraft.client.gui.DrawContext;

public final class AtmosphereRenderer {
	private AtmosphereRenderer() {}

	public static void draw(DrawContext c, int w, int h) {
		AtmosphereFeature f = Features.ATMOSPHERE;
		if (!f.enabled) return;

		int tintA = (int) (f.tint.value / 100.0 * 255.0 * 0.55);
		if (tintA > 0) {
			int rgb = Gfx.hsb((float) (f.hue.value / 360.0), 0.75f, 1f) & 0xFFFFFF;
			c.fill(0, 0, w, h, (tintA << 24) | rgb);
		}

		double v = f.vignette.value / 100.0;
		if (v <= 0) return;
		int steps = 20;
		int sx = Math.max(2, w / (steps * 6));
		int sy = Math.max(2, h / (steps * 6));
		for (int i = 0; i < steps; i++) {
			float k = 1f - i / (float) steps;
			int a = (int) (v * 0.7 * 255 * k * k);
			if (a <= 0) continue;
			int col = a << 24;
			c.fill(i * sx, 0, (i + 1) * sx, h, col);
			c.fill(w - (i + 1) * sx, 0, w - i * sx, h, col);
			c.fill(0, i * sy, w, (i + 1) * sy, col);
			c.fill(0, h - (i + 1) * sy, w, h - i * sy, col);
		}
	}
}
