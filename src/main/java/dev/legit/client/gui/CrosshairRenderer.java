package dev.legit.client.gui;

import dev.legit.client.feature.Features;
import dev.legit.client.feature.impl.CrosshairFeature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CrosshairRenderer {
	private CrosshairRenderer() {}

	public static void draw(DrawContext c) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.options == null || !mc.options.getPerspective().isFirstPerson()) return;

		CrosshairFeature f = Features.CROSSHAIR;
		int cx = c.getScaledWindowWidth() / 2;
		int cy = c.getScaledWindowHeight() / 2;
		int gap = (int) f.gap.value;
		int len = (int) f.length.value;
		int th = (int) f.thickness.value;
		int col = f.themeColor.value ? (Gfx.accentA() | 0xFF000000) : 0xFFFFFFFF;
		int half = th / 2;

		if (f.outline.value) {
			int o = 0xC0000000;
			bars(c, cx, cy, gap - 1, len + 2, th + 2, half + 1, o);
			if (f.dot.value) c.fill(cx - half - 1, cy - half - 1, cx - half + th + 1, cy - half + th + 1, o);
		}
		bars(c, cx, cy, gap, len, th, half, col);
		if (f.dot.value) c.fill(cx - half, cy - half, cx - half + th, cy - half + th, col);
	}

	private static void bars(DrawContext c, int cx, int cy, int gap, int len, int th, int half, int color) {
		gap = Math.max(0, gap);
		// левая, правая, верхняя, нижняя
		c.fill(cx - gap - len, cy - half, cx - gap, cy - half + th, color);
		c.fill(cx + gap, cy - half, cx + gap + len, cy - half + th, color);
		c.fill(cx - half, cy - gap - len, cx - half + th, cy - gap, color);
		c.fill(cx - half, cy + gap, cx - half + th, cy + gap + len, color);
	}
}
