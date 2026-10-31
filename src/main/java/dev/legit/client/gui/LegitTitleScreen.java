package dev.legit.client.gui;

import dev.legit.client.feature.Features;
import dev.legit.client.feature.impl.MainMenuFeature;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Собственное главное меню: анимированный фон в цветах темы, колонка кнопок, карточка игрока. */
public class LegitTitleScreen extends Screen {
	private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm");

	private record Btn(String label, Runnable action, int col, int span) {}

	private static final class Slot {
		int x, y, w, h;
		float hover;
		Btn btn;
	}

	private final List<Slot> slots = new ArrayList<>();
	private float open;
	private long last = System.nanoTime();
	private final long born = System.nanoTime();

	public LegitTitleScreen() {
		super(Text.literal("Legit Client"));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	protected void init() {
		slots.clear();
		List<Btn> list = new ArrayList<>();
		list.add(new Btn("Одиночная игра", () -> client.setScreen(new SelectWorldScreen(this)), 0, 2));
		list.add(new Btn("Сетевая игра", () -> client.setScreen(new MultiplayerScreen(this)), 0, 2));
		list.add(new Btn("Настройки клиента", () -> client.setScreen(new ClickGui()), 0, 2));
		list.add(new Btn("Настройки...", () -> client.setScreen(new OptionsScreen(this, client.options)), 0, 1));
		list.add(new Btn("Выйти из игры", () -> client.scheduleStop(), 1, 1));
		for (Btn b : list) {
			Slot s = new Slot();
			s.btn = b;
			slots.add(s);
		}
	}

	private MainMenuFeature cfg() {
		return Features.MAIN_MENU;
	}

	@Override
	public void render(DrawContext c, int mouseX, int mouseY, float delta) {
		long now = System.nanoTime();
		float dt = Math.min(0.1f, (now - last) / 1_000_000_000f);
		last = now;
		open = Math.min(1f, open + dt * 3f);
		float e = Gfx.easeOut(open);
		float t = (now - born) / 1_000_000_000f;
		float k = Math.min(1f, dt * 16f);
		MainMenuFeature m = cfg();

		drawBackground(c, t, m);
		if (m.stars.value) StarField.draw(c, width, height, t);
		if (Features.SNOW.enabled && Features.SNOW.inMenus.value) SnowRenderer.draw(c, width, height);

		// ---- раскладка ----
		int menuW = (int) m.width.value;
		int bh = (int) m.buttonHeight.value;
		int gap = 6;
		int rowsCount = 4; // 3 полных ряда + ряд из двух половинок
		int titleBlock = 62;
		int total = titleBlock + rowsCount * bh + (rowsCount - 1) * gap;

		int baseX = m.alignRight.value ? width - menuW - Math.max(24, width / 14) : (width - menuW) / 2;
		int baseY = Math.max(16, (height - total) / 2) + (int) ((1f - e) * 14f);

		drawTitle(c, baseX, baseY, menuW, e);

		int y = baseY + titleBlock;
		int half = (menuW - gap) / 2;
		int row = 0;
		for (Slot s : slots) {
			Btn b = s.btn;
			if (b.span() == 2) {
				s.x = baseX;
				s.w = menuW;
				s.y = y + row * (bh + gap);
				row++;
			} else {
				s.x = b.col() == 0 ? baseX : baseX + half + gap;
				s.w = half;
				s.y = y + row * (bh + gap);
				if (b.col() == 1) row++;
			}
			s.h = bh;
		}

		for (Slot s : slots) {
			boolean over = mouseX >= s.x && mouseX < s.x + s.w && mouseY >= s.y && mouseY < s.y + s.h;
			s.hover += ((over ? 1f : 0f) - s.hover) * k;
			drawButton(c, s, e, m);
		}

		if (m.playerCard.value) drawPlayerCard(c, e, m);
		drawFooter(c, e, m);
	}

	// ------------------------------------------------------------------ background

	private void drawBackground(DrawContext c, float t, MainMenuFeature m) {
		float dark = (float) (m.darkness.value / 100.0);
		int base = 0xFF07070D;
		int top = Gfx.lerp(base, Gfx.accentA() | 0xFF000000, 0.30f * (1f - dark) + 0.05f);
		int bot = Gfx.lerp(base, Gfx.accentB() | 0xFF000000, 0.26f * (1f - dark) + 0.04f);

		int strip = 4;
		for (int y = 0; y < height; y += strip) {
			float f = (float) y / Math.max(1, height - 1);
			c.fill(0, y, width, Math.min(height, y + strip), Gfx.lerp(top, bot, f));
		}

		if (m.orbs.value) {
			float sp = (float) m.orbSpeed.value * 0.12f;
			orb(c, width * (0.25f + 0.18f * (float) Math.sin(t * sp)), height * (0.35f + 0.20f * (float) Math.cos(t * sp * 0.8f)),
				Math.min(width, height) * 0.38f, Gfx.accentA(), 0.07f);
			orb(c, width * (0.72f + 0.16f * (float) Math.cos(t * sp * 0.9f)), height * (0.68f + 0.18f * (float) Math.sin(t * sp * 1.1f)),
				Math.min(width, height) * 0.34f, Gfx.accentB(), 0.07f);
			orb(c, width * (0.50f + 0.25f * (float) Math.sin(t * sp * 0.6f + 2f)), height * (0.20f + 0.12f * (float) Math.cos(t * sp * 0.7f)),
				Math.min(width, height) * 0.22f, Gfx.lerp(Gfx.accentA(), Gfx.accentB(), 0.5f), 0.06f);
		}

		// лёгкое затемнение по краям
		c.fill(0, 0, width, 1, 0x30000000);
		c.fill(0, height - 1, width, height, 0x30000000);
	}

	/** Мягкое свечение: несколько вложенных кругов с малой прозрачностью. */
	private void orb(DrawContext c, float cx, float cy, float radius, int color, float alphaEach) {
		int rings = 7;
		int rgb = color & 0xFFFFFF;
		int a = Math.max(2, (int) (alphaEach * 255));
		for (int i = 0; i < rings; i++) {
			float r = radius * (1f - i / (float) rings);
			disc(c, cx, cy, r, (a << 24) | rgb);
		}
	}

	private void disc(DrawContext c, float cx, float cy, float r, int argb) {
		int ri = (int) r;
		for (int dy = -ri; dy < ri; dy += 2) {
			double half = Math.sqrt(Math.max(0, (double) r * r - (double) dy * dy));
			c.fill((int) (cx - half), (int) cy + dy, (int) (cx + half), (int) cy + dy + 2, argb);
		}
	}

	// ------------------------------------------------------------------ pieces

	private void drawTitle(DrawContext c, int x, int y, int w, float e) {
		var tr = textRenderer;
		String a = "Legit", b = " Client";
		float s = 2f;
		c.getMatrices().push();
		c.getMatrices().scale(s, s, 1f);
		c.drawText(tr, a, (int) (x / s), (int) (y / s), Gfx.withAlpha(Gfx.accentA(), e), true);
		c.drawText(tr, b, (int) (x / s) + tr.getWidth(a), (int) (y / s), Gfx.withAlpha(Gfx.TEXT, e), true);
		c.getMatrices().pop();

		String name = client != null && client.getSession() != null ? client.getSession().getUsername() : "Player";
		c.drawText(tr, "Добро пожаловать, " + name + "!", x, y + 24, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
		Gfx.gradientBar(c, x, y + 40, Math.min(w, 90), 2, e);
	}

	private void drawButton(DrawContext c, Slot s, float e, MainMenuFeature m) {
		float r = 8f;
		float h = s.hover;

		if (h > 0.02f) { // свечение вокруг кнопки при наведении
			Gfx.rrect(c, s.x - 1, s.y - 1, s.w + 2, s.h + 2, r + 1, Gfx.withAlpha(Gfx.lerp(Gfx.accentA(), Gfx.accentB(), 0.5f), e * h * 0.9f));
		}
		int bg = Gfx.lerp(0xE0141420, 0xF0201C34, h);
		Gfx.rrect(c, s.x, s.y, s.w, s.h, r, Gfx.withAlpha(bg, e * Features.THEME.panelAlpha() / 255f));

		// акцентная полоска слева
		int bar = Gfx.lerp(Gfx.OFF, Gfx.accentA(), h);
		c.fill(s.x + 5, s.y + s.h / 2 - 4, s.x + 7, s.y + s.h / 2 + 4, Gfx.withAlpha(bar, e));

		int tx = s.x + 14 + (int) (h * 3f);
		c.drawText(textRenderer, s.btn.label(), tx, s.y + (s.h - 8) / 2, Gfx.withAlpha(Gfx.lerp(Gfx.TEXT, 0xFFFFFFFF, h), e), true);
		if (s.btn.span() == 2) {
			c.drawText(textRenderer, ">", s.x + s.w - 14 - (int) (h * 2f), s.y + (s.h - 8) / 2,
				Gfx.withAlpha(Gfx.lerp(Gfx.TEXT_DIM, Gfx.accentB(), h), e), true);
		}
	}

	private void drawPlayerCard(DrawContext c, float e, MainMenuFeature m) {
		var tr = textRenderer;
		String name = client != null && client.getSession() != null ? client.getSession().getUsername() : "Player";
		int w = Math.max(104, tr.getWidth(name) + 52);
		int h = 30;
		int x = width - w - 12, y = 12;
		float r = 8f;

		Gfx.rrect(c, x, y, w, h, r, Gfx.withAlpha(0xD0141420, e));
		Gfx.rrect(c, x + 6, y + 5, 20, 20, 10, Gfx.withAlpha(Gfx.accentA(), e));
		String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
		c.drawText(tr, initial, x + 6 + (20 - tr.getWidth(initial)) / 2, y + 11, Gfx.withAlpha(0xFFFFFFFF, e), true);
		Gfx.rrect(c, x + 21, y + 20, 6, 6, 3, Gfx.withAlpha(0xFF4ADE80, e));

		c.drawText(tr, name, x + 32, y + 6, Gfx.withAlpha(Gfx.TEXT, e), true);
		c.drawText(tr, "Профиль", x + 32, y + 17, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
	}

	private void drawFooter(DrawContext c, float e, MainMenuFeature m) {
		var tr = textRenderer;
		String ver = "Legit Client 1.0  |  Minecraft " + client.getGameVersion();
		c.drawText(tr, ver, 10, height - 14, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);

		String hint = "Меню клиента: " + net.minecraft.client.util.InputUtil.fromKeyCode(dev.legit.client.Keys.gui(), 0).getLocalizedText().getString();
		c.drawText(tr, hint, width - tr.getWidth(hint) - 10, height - 14, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);

		if (m.clock.value) {
			String time = LocalTime.now().format(CLOCK);
			c.drawText(tr, time, 10, 10, Gfx.withAlpha(Gfx.TEXT, e), true);
		}
	}

	// ------------------------------------------------------------------ input

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0) {
			for (Slot s : slots) {
				if (mx >= s.x && mx < s.x + s.w && my >= s.y && my < s.y + s.h) {
					s.btn.action().run();
					return true;
				}
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == dev.legit.client.Keys.gui()) {
			client.setScreen(new ClickGui());
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
