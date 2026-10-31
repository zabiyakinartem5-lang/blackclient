package dev.legit.client.gui;

import dev.legit.client.Config;
import dev.legit.client.gui.hud.HudElement;
import dev.legit.client.gui.hud.HudElements;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** Экран, где блоки HUD перетаскиваются мышью. ПКМ по блоку возвращает его на место. */
public class HudEditor extends Screen {
	private HudElement dragging;
	private float grabX, grabY;
	private boolean snapped;

	private int resetX, resetY, resetW = 70, resetH = 16;

	public HudEditor() {
		super(Text.literal("HUD Editor"));
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void close() {
		Config.save();
		if (client != null) client.setScreen(new ClickGui());
	}

	private float elLeft(HudElement e) {
		return e.left(width);
	}

	private float elTop(HudElement e) {
		return e.top(height);
	}

	private float elW(HudElement e) {
		return Math.max(36f, e.w * e.scale);
	}

	private float elH(HudElement e) {
		return Math.max(14f, e.h * e.scale);
	}

	private boolean inside(HudElement e, double mx, double my) {
		float l = elLeft(e), t = elTop(e);
		return mx >= l && mx <= l + elW(e) && my >= t && my <= t + elH(e);
	}

	@Override
	public void render(DrawContext c, int mouseX, int mouseY, float delta) {
		c.fill(0, 0, width, height, 0x66050510);

		if (snapped && dragging != null) {
			c.fill(width / 2, 0, width / 2 + 1, height, Gfx.withAlpha(Gfx.accentB(), 0.8f));
		}

		HudElement hover = null;
		for (int i = HudElements.ALL.size() - 1; i >= 0; i--) {
			HudElement e = HudElements.ALL.get(i);
			if (e.visible.getAsBoolean() && inside(e, mouseX, mouseY)) {
				hover = e;
				break;
			}
		}

		for (HudElement e : HudElements.ALL) {
			if (!e.visible.getAsBoolean()) continue;
			int l = Math.round(elLeft(e)), t = Math.round(elTop(e));
			int w = Math.round(elW(e)), h = Math.round(elH(e));
			boolean active = e == dragging || e == hover;
			int col = active ? Gfx.accentB() : Gfx.withAlpha(Gfx.accentA(), 0.8f);

			c.fill(l, t, l + w, t + h, active ? 0x30FFFFFF : 0x18FFFFFF);
			c.fill(l, t, l + w, t + 1, col);
			c.fill(l, t + h - 1, l + w, t + h, col);
			c.fill(l, t, l + 1, t + h, col);
			c.fill(l + w - 1, t, l + w, t + h, col);

			int ly = t >= 11 ? t - 10 : t + h + 2;
			c.drawText(textRenderer, e.label, l, ly, col, true);
		}

		String title = "HUD Editor";
		String hint = "Drag blocks with LMB  |  RMB - reset block  |  ESC - back";
		c.drawText(textRenderer, title, (width - textRenderer.getWidth(title)) / 2, height / 2 - 14, Gfx.TEXT, true);
		c.drawText(textRenderer, hint, (width - textRenderer.getWidth(hint)) / 2, height / 2 - 2, Gfx.TEXT_DIM, true);

		// кнопка Reset all
		resetX = (width - resetW) / 2;
		resetY = height / 2 + 12;
		boolean rh = mouseX >= resetX && mouseX < resetX + resetW && mouseY >= resetY && mouseY < resetY + resetH;
		Gfx.rrect(c, resetX, resetY, resetW, resetH, 5, rh ? Gfx.accentA() : Gfx.header());
		String rt = "Reset all";
		c.drawText(textRenderer, rt, resetX + (resetW - textRenderer.getWidth(rt)) / 2, resetY + 4, Gfx.TEXT, true);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0 && mx >= resetX && mx < resetX + resetW && my >= resetY && my < resetY + resetH) {
			HudElements.resetAll();
			return true;
		}
		for (int i = HudElements.ALL.size() - 1; i >= 0; i--) {
			HudElement e = HudElements.ALL.get(i);
			if (!e.visible.getAsBoolean() || !inside(e, mx, my)) continue;
			if (button == 0) {
				dragging = e;
				grabX = (float) mx - elLeft(e);
				grabY = (float) my - elTop(e);
			} else if (button == 1) {
				e.reset();
			}
			return true;
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
		if (dragging == null || button != 0) return super.mouseDragged(mx, my, button, dx, dy);

		HudElement e = dragging;
		float ww = e.w * e.scale;
		float hh = e.h * e.scale;
		float newLeft = (float) mx - grabX;
		float newTop = (float) my - grabY;

		// примагничивание к центру экрана по горизонтали
		snapped = false;
		if (Math.abs(newLeft + ww / 2f - width / 2f) < 6f) {
			newLeft = width / 2f - ww / 2f;
			snapped = true;
		}
		newLeft = Math.max(0f, Math.min(width - ww, newLeft));
		newTop = Math.max(0f, Math.min(height - hh, newTop));

		e.fx = (newLeft + e.anchorOffset()) / width;
		e.fy = newTop / height;
		return true;
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button) {
		if (dragging != null) {
			dragging = null;
			snapped = false;
			Config.save();
		}
		return super.mouseReleased(mx, my, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == dev.legit.client.Keys.gui()) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
