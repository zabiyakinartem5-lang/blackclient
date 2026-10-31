package dev.legit.client.gui;

import dev.legit.client.feature.Features;
import net.minecraft.client.gui.DrawContext;

/** Помощники для отрисовки: скруглённые прямоугольники, градиенты, цвета темы. */
public final class Gfx {
	public static final int OFF = 0xFF3A3A4A;
	public static final int TEXT = 0xFFF2F2F7;
	public static final int TEXT_DIM = 0xFF9A9AB0;
	public static final int ROW_HOVER = 0x22FFFFFF;

	private Gfx() {}

	// ---------- цвета темы (настраиваются в панели Client → Theme) ----------

	public static int accentA() {
		return Features.THEME.colorA();
	}

	public static int accentB() {
		return Features.THEME.colorB();
	}

	public static int panel() {
		return (Features.THEME.panelAlpha() << 24) | 0x121218;
	}

	/** Фон блоков HUD с прозрачностью из Theme. */
	public static int hudBg() {
		return (Features.THEME.hudAlpha() << 24) | 0x101018;
	}

	public static int header() {
		return (Features.THEME.panelAlpha() << 24) | 0x1B1B26;
	}

	/** HSB → ARGB (без java.awt, чтобы не грузить AWT в игре). h: 0..1, s: 0..1, b: 0..1 */
	public static int hsb(float h, float s, float b) {
		h = h - (float) Math.floor(h);
		float r = 0, g = 0, bl = 0;
		float hh = h * 6f;
		int i = (int) Math.floor(hh);
		float f = hh - i;
		float p = b * (1f - s);
		float q = b * (1f - s * f);
		float t = b * (1f - s * (1f - f));
		switch (i % 6) {
			case 0 -> { r = b; g = t; bl = p; }
			case 1 -> { r = q; g = b; bl = p; }
			case 2 -> { r = p; g = b; bl = t; }
			case 3 -> { r = p; g = q; bl = b; }
			case 4 -> { r = t; g = p; bl = b; }
			default -> { r = b; g = p; bl = q; }
		}
		return 0xFF000000 | ((int) (r * 255 + 0.5f) << 16) | ((int) (g * 255 + 0.5f) << 8) | (int) (bl * 255 + 0.5f);
	}

	// ---------- примитивы ----------

	public static void rrect(DrawContext c, float x, float y, float w, float h, float r, int color) {
		r *= Features.THEME.radiusScale();
		int xi = Math.round(x), yi = Math.round(y), wi = Math.round(w), hi = Math.round(h);
		int ri = (int) Math.min(r, Math.min(wi, hi) / 2f);
		for (int i = 0; i < ri; i++) {
			double dy = ri - i - 0.5;
			int inset = (int) Math.round(ri - Math.sqrt(Math.max(0, ri * ri - dy * dy)));
			c.fill(xi + inset, yi + i, xi + wi - inset, yi + i + 1, color);
			c.fill(xi + inset, yi + hi - 1 - i, xi + wi - inset, yi + hi - i, color);
		}
		c.fill(xi, yi + ri, xi + wi, yi + hi - ri, color);
	}

	/** Горизонтальный градиент accentA → accentB. */
	public static void gradientBar(DrawContext c, int x, int y, int w, int h, float alpha) {
		int seg = 3;
		int a = accentA(), b = accentB();
		for (int i = 0; i < w; i += seg) {
			float t = (float) i / Math.max(1, w - 1);
			int col = withAlpha(lerp(a, b, t), alpha);
			c.fill(x + i, y, Math.min(x + i + seg, x + w), y + h, col);
		}
	}

	public static int lerp(int a, int b, float t) {
		t = Math.max(0f, Math.min(1f, t));
		int aa = (a >>> 24) & 255, ar = (a >> 16) & 255, ag = (a >> 8) & 255, ab = a & 255;
		int ba = (b >>> 24) & 255, br = (b >> 16) & 255, bg = (b >> 8) & 255, bb = b & 255;
		return ((int) (aa + (ba - aa) * t) << 24)
			| ((int) (ar + (br - ar) * t) << 16)
			| ((int) (ag + (bg - ag) * t) << 8)
			| (int) (ab + (bb - ab) * t);
	}

	/** Умножает альфу цвета на коэффициент (минимум 8, чтобы текст не стал непрозрачным). */
	public static int withAlpha(int color, float mul) {
		int a = (int) (((color >>> 24) & 255) * Math.max(0f, Math.min(1f, mul)));
		a = Math.max(8, a);
		return (a << 24) | (color & 0xFFFFFF);
	}

	public static float easeOut(float t) {
		float u = 1f - Math.max(0f, Math.min(1f, t));
		return 1f - u * u * u;
	}
}
