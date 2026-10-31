package dev.legit.client.gui;

import net.minecraft.client.gui.DrawContext;

import java.util.Random;

/** Мерцающее звёздное небо с редкими падающими звёздами (для главного меню). */
public final class StarField {
	private static final int N = 170;
	private static final float[] X = new float[N];
	private static final float[] Y = new float[N];
	private static final float[] P = new float[N];
	private static final float[] S = new float[N];
	private static boolean init;

	private static final Random RND = new Random();
	private static float shootT = -1f, sx, sy, nextAt = 3f, lastT;

	private StarField() {}

	public static void draw(DrawContext c, int w, int h, float t) {
		if (!init) {
			Random r = new Random(42);
			for (int i = 0; i < N; i++) {
				X[i] = r.nextFloat();
				Y[i] = r.nextFloat();
				P[i] = r.nextFloat() * 6.2832f;
				S[i] = 0.2f + r.nextFloat() * 0.8f;
			}
			init = true;
		}

		for (int i = 0; i < N; i++) {
			float tw = 0.5f + 0.5f * (float) Math.sin(t * (0.8f + S[i] * 1.5f) + P[i]);
			int a = (int) (40 + 200 * tw * S[i]);
			int px = (int) (X[i] * w), py = (int) (Y[i] * h);
			int s = S[i] > 0.85f ? 2 : 1;
			c.fill(px, py, px + s, py + s, (a << 24) | 0xFFFFFF);
		}

		float dt = Math.max(0f, Math.min(0.1f, t - lastT));
		lastT = t;
		if (shootT < 0f && t >= nextAt) {
			shootT = 0f;
			sx = w * (0.1f + RND.nextFloat() * 0.6f);
			sy = h * RND.nextFloat() * 0.35f;
			nextAt = t + 4f + RND.nextFloat() * 6f;
		}
		if (shootT >= 0f) {
			shootT += dt * 1.4f;
			for (int k = 0; k < 14; k++) {
				float p = shootT - k * 0.012f;
				if (p < 0f) break;
				int px = (int) (sx + p * w * 0.5f), py = (int) (sy + p * h * 0.35f);
				int a = (int) ((1f - k / 14f) * 220 * (1f - shootT * 0.5f));
				c.fill(px, py, px + 2, py + 2, (Math.max(0, a) << 24) | 0xFFFFFF);
			}
			if (shootT > 1f) shootT = -1f;
		}
	}
}
