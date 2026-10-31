package dev.legit.client.gui;

import dev.legit.client.feature.Features;
import dev.legit.client.feature.impl.SnowFeature;
import net.minecraft.client.gui.DrawContext;

import java.util.Random;

/** Снежинки поверх экрана: простые квадратики, плавно падающие и покачивающиеся. */
public final class SnowRenderer {
	private static final int MAX = 400;
	private static final float[] X = new float[MAX];
	private static final float[] Y = new float[MAX];
	private static final float[] V = new float[MAX];
	private static final float[] P = new float[MAX];
	private static final float[] S = new float[MAX];
	private static boolean init;
	private static long last = System.nanoTime();

	private SnowRenderer() {}

	public static void draw(DrawContext c, int w, int h) {
		SnowFeature f = Features.SNOW;
		if (!init) {
			Random r = new Random(7);
			for (int i = 0; i < MAX; i++) {
				X[i] = r.nextFloat();
				Y[i] = r.nextFloat();
				V[i] = r.nextFloat() * 0.06f;
				P[i] = r.nextFloat() * 6.2832f;
				S[i] = 0.5f + r.nextFloat() * 0.5f;
			}
			init = true;
		}

		long now = System.nanoTime();
		float dt = Math.min(0.1f, (now - last) / 1_000_000_000f);
		last = now;
		float time = now / 1_000_000_000f;

		int n = Math.min(MAX, (int) f.amount.value);
		float speed = (float) f.speed.value;
		float size = (float) f.size.value;

		for (int i = 0; i < n; i++) {
			Y[i] += dt * (0.05f + V[i]) * speed * 0.35f;
			X[i] += (float) Math.sin(time * 0.9f + P[i]) * 0.012f * dt;
			if (Y[i] > 1.02f) {
				Y[i] = -0.02f;
				X[i] = (X[i] + 0.37f) % 1f;
			}
			if (X[i] < 0f) X[i] += 1f;
			if (X[i] > 1f) X[i] -= 1f;

			int px = (int) (X[i] * w), py = (int) (Y[i] * h);
			int s = Math.max(1, Math.round(S[i] * size));
			int a = (int) (90 + 140 * S[i]);
			c.fill(px, py, px + s, py + s, (a << 24) | 0xFFFFFF);
		}
	}
}
