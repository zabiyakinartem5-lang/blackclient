package dev.legit.client.gui;

import dev.legit.client.Config;
import dev.legit.client.data.Friends;
import dev.legit.client.data.Profiles;
import dev.legit.client.data.Waypoints;
import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.Features;
import dev.legit.client.feature.setting.BindSetting;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import dev.legit.client.feature.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Компактное меню: небольшое окно, вкладки в два ряда, список модулей с прокруткой, вкладки Friends / Marks / Configs. */
public class ClickGui extends Screen {
	private static final int W = 232;
	private static final int H = 224;
	private static final int HEADER_H = 22;
	private static final int TAB_ROW = 16;
	private static final int TABS_H = TAB_ROW * 2;
	private static final int FOOTER_H = 22;
	private static final int ROW_H = 20;
	private static final int ENTRY_H = 24;
	private static final int INPUT_H = 22;
	private static final int PAD = 6;

	/** Запоминаем вкладку и прокрутку между открытиями. */
	private static Category tab = Category.VISUAL;
	private static float scroll;

	private final List<Hit> hits = new ArrayList<>();
	private float open;
	private long last = System.nanoTime();
	private float scrollAnim;

	private BindSetting listening;
	private NumSetting dragging;
	private Hit dragHit;

	private TextFieldWidget input;
	private List<String> profileCache = new ArrayList<>();
	private String status = "";
	private long statusUntil;

	private int x0, y0, listTop, listBottom;
	private float contentH;

	private record Hit(int x, int y, int w, int h, Runnable left, Runnable right, NumSetting slider) {
		boolean contains(double mx, double my) {
			return mx >= x && mx < x + w && my >= y && my < y + h;
		}
	}

	/** Строка списка во вкладках Friends / Marks / Configs. */
	private record Entry(String title, String sub, int dot, String actLabel, Runnable act, Runnable remove) {}

	public ClickGui() {
		super(Text.literal("Legit Client"));
		scrollAnim = scroll;
	}

	@Override
	protected void init() {
		input = new TextFieldWidget(textRenderer, 0, 0, 100, 14, Text.empty());
		input.setMaxLength(24);
		input.setVisible(false);
		addSelectableChild(input);
		refreshProfiles();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void close() {
		Config.save();
		super.close();
	}

	private void setStatus(String s) {
		status = s;
		statusUntil = System.currentTimeMillis() + 3000;
	}

	private void refreshProfiles() {
		profileCache = Profiles.list();
	}

	// ------------------------------------------------------------------ render

	@Override
	public void render(DrawContext c, int mouseX, int mouseY, float delta) {
		long now = System.nanoTime();
		float dt = Math.min(0.1f, (now - last) / 1_000_000_000f);
		last = now;
		open = Math.min(1f, open + dt * 6f);
		float e = Gfx.easeOut(open);
		float k = Math.min(1f, dt * 14f);

		hits.clear();
		c.fill(0, 0, width, height, Gfx.withAlpha(0x88050510, e));

		x0 = (width - W) / 2;
		y0 = (int) ((height - H) / 2 + (1f - e) * 12f);

		Gfx.rrect(c, x0 - 1, y0 - 1, W + 2, H + 2, 9, Gfx.withAlpha(0x40000000, e));
		Gfx.rrect(c, x0, y0, W, H, 8, Gfx.withAlpha(Gfx.panel(), e));

		drawHeader(c, e);
		drawTabs(c, mouseX, mouseY, e);

		listTop = y0 + HEADER_H + 3 + TABS_H + 4;
		listBottom = y0 + H - FOOTER_H;
		drawList(c, mouseX, mouseY, e, k, dt, delta);
		drawFooter(c, mouseX, mouseY, e);
	}

	private void drawHeader(DrawContext c, float e) {
		Gfx.rrect(c, x0, y0, W, HEADER_H, 8, Gfx.withAlpha(Gfx.header(), e));
		c.fill(x0, y0 + HEADER_H - 8, x0 + W, y0 + HEADER_H, Gfx.withAlpha(Gfx.header(), e));
		Gfx.gradientBar(c, x0 + 8, y0 + HEADER_H - 2, W - 16, 2, e);

		String a = "Legit", b = " Client";
		c.drawText(textRenderer, a, x0 + PAD + 2, y0 + 7, Gfx.withAlpha(Gfx.accentA(), e), true);
		c.drawText(textRenderer, b, x0 + PAD + 2 + textRenderer.getWidth(a), y0 + 7, Gfx.withAlpha(Gfx.TEXT, e), true);

		boolean showStatus = System.currentTimeMillis() < statusUntil;
		String right = showStatus ? status : "1.0";
		int col = showStatus ? Gfx.accentB() : Gfx.TEXT_DIM;
		c.drawText(textRenderer, right, x0 + W - PAD - 2 - textRenderer.getWidth(right), y0 + 7, Gfx.withAlpha(col, e), true);
	}

	private void drawTabs(DrawContext c, int mx, int my, float e) {
		Category[] cats = Category.values();
		int perRow = 4, gap = 2;
		int tw = (W - PAD * 2 - gap * (perRow - 1)) / perRow;
		for (int i = 0; i < cats.length; i++) {
			Category cat = cats[i];
			int tx = x0 + PAD + (i % perRow) * (tw + gap);
			int ty = y0 + HEADER_H + 3 + (i / perRow) * TAB_ROW;
			boolean sel = cat == tab;
			boolean hover = mx >= tx && mx < tx + tw && my >= ty && my < ty + TAB_ROW - 2;
			if (sel || hover) {
				Gfx.rrect(c, tx, ty, tw, TAB_ROW - 2, 5,
					Gfx.withAlpha(sel ? Gfx.accentA() : Gfx.ROW_HOVER, e * (sel ? 0.9f : 1f)));
			}
			int col = sel ? 0xFFFFFFFF : Gfx.TEXT_DIM;
			c.drawText(textRenderer, cat.label, tx + (tw - textRenderer.getWidth(cat.label)) / 2, ty + 3, Gfx.withAlpha(col, e), true);
			final Category target = cat;
			hits.add(new Hit(tx, ty, tw, TAB_ROW - 2, () -> selectTab(target), null, null));
		}
	}

	private void selectTab(Category cat) {
		tab = cat;
		scroll = 0;
		scrollAnim = 0;
		if (input != null) {
			input.setText("");
			input.setFocused(false);
		}
		setFocused(null);
		if (cat == Category.CONFIGS) refreshProfiles();
	}

	private void drawFooter(DrawContext c, int mx, int my, float e) {
		int fy = y0 + H - FOOTER_H + 3;
		int bw = (W - PAD * 3) / 2, bh = 16;
		button(c, x0 + PAD, fy, bw, bh, "Edit HUD", mx, my, e, () -> client.setScreen(new HudEditor()));
		button(c, x0 + PAD * 2 + bw, fy, bw, bh, "Close", mx, my, e, this::close);
	}

	private void button(DrawContext c, int x, int y, int w, int h, String text, int mx, int my, float e, Runnable action) {
		boolean hover = mx >= x && mx < x + w && my >= y && my < y + h;
		Gfx.rrect(c, x, y, w, h, 5, Gfx.withAlpha(hover ? Gfx.accentA() : Gfx.header(), e));
		c.drawText(textRenderer, text, x + (w - textRenderer.getWidth(text)) / 2, y + 4, Gfx.withAlpha(Gfx.TEXT, e), true);
		hits.add(new Hit(x, y, w, h, action, null, null));
	}

	// ------------------------------------------------------------------ list

	private List<Feature> current() {
		List<Feature> list = new ArrayList<>();
		for (Feature f : Features.ALL) if (f.category == tab) list.add(f);
		return list;
	}

	private float settingsHeight(Feature f) {
		float h = 0;
		for (Setting s : f.settings) h += s.height();
		return h + (f.settings.isEmpty() ? 0 : 3);
	}

	private void drawList(DrawContext c, int mx, int my, float e, float k, float dt, float delta) {
		List<Feature> features = current();
		boolean custom = tab.isCustom();
		List<Entry> entries = custom ? entries() : List.of();

		contentH = 0;
		for (Feature f : features) {
			f.expandAnim += ((f.expanded ? 1f : 0f) - f.expandAnim) * k;
			contentH += ROW_H + settingsHeight(f) * f.expandAnim;
		}
		if (custom) contentH += INPUT_H + (entries.isEmpty() ? 18 : entries.size() * ENTRY_H) + 4;

		float viewH = listBottom - listTop;
		float maxScroll = Math.max(0f, contentH - viewH);
		scroll = Math.max(0f, Math.min(maxScroll, scroll));
		scrollAnim += (scroll - scrollAnim) * Math.min(1f, dt * 16f);

		c.enableScissor(x0 + 2, listTop, x0 + W - 2, listBottom);
		float cy = listTop - scrollAnim;
		for (Feature f : features) {
			f.anim += ((f.enabled ? 1f : 0f) - f.anim) * k;
			drawFeatureRow(c, f, (int) cy, mx, my, e);
			cy += ROW_H;

			float sh = settingsHeight(f) * f.expandAnim;
			if (sh > 0.5f) {
				// вложенная область обрезки пересекается с внешней и снимается своим disableScissor
				c.enableScissor(x0 + 2, Math.max(listTop, (int) cy), x0 + W - 2, Math.min(listBottom, (int) (cy + sh)));
				float sy = cy;
				boolean interactive = f.expandAnim > 0.9f && f.expanded;
				for (Setting s : f.settings) {
					drawSetting(c, s, (int) sy, mx, my, e, k, interactive);
					sy += s.height();
				}
				c.disableScissor();
			}
			cy += sh;
		}

		if (custom) {
			drawCustom(c, cy, entries, mx, my, e, delta);
		} else if (input != null) {
			input.setVisible(false);
			input.setFocused(false);
		}
		c.disableScissor();

		if (maxScroll > 0.5f) {
			float thumbH = Math.max(16f, viewH * (viewH / contentH));
			float thumbY = listTop + (viewH - thumbH) * (scrollAnim / maxScroll);
			Gfx.rrect(c, x0 + W - 5, thumbY, 2, thumbH, 1, Gfx.withAlpha(Gfx.accentA(), e * 0.8f));
		}
	}

	/** Клик по строке доступен только если она видна в окне списка. */
	private void addListHit(Hit h) {
		int top = Math.max(h.y(), listTop);
		int bottom = Math.min(h.y() + h.h(), listBottom);
		if (bottom - top <= 2) return;
		hits.add(new Hit(h.x(), top, h.w(), bottom - top, h.left(), h.right(), h.slider()));
	}

	private void drawFeatureRow(DrawContext c, Feature f, int y, int mx, int my, float e) {
		int rx = x0 + PAD, rw = W - PAD * 2 - 4, rh = ROW_H - 2;
		if (y + rh < listTop || y > listBottom) return;

		boolean hover = mx >= rx && mx < rx + rw && my >= Math.max(y, listTop) && my < Math.min(y + rh, listBottom);
		if (hover) Gfx.rrect(c, rx, y, rw, rh, 5, Gfx.withAlpha(Gfx.ROW_HOVER, e));

		int nameCol = Gfx.lerp(Gfx.TEXT_DIM, Gfx.TEXT, f.alwaysOn ? 1f : f.anim);
		c.drawText(textRenderer, f.name, rx + 6, y + 5, Gfx.withAlpha(nameCol, e), true);

		int pw = 24, ph = 12;
		int px = rx + rw - pw - 4, py = y + (rh - ph) / 2;
		Runnable expand = () -> f.expanded = !f.expanded;

		if (f.alwaysOn) {
			String sign = f.expanded ? "-" : "+";
			c.drawText(textRenderer, sign, px + pw - 6, y + 5, Gfx.withAlpha(Gfx.accentB(), e), true);
			addListHit(new Hit(rx, y, rw, rh, expand, expand, null));
			return;
		}

		int track = Gfx.lerp(Gfx.OFF, Gfx.accentA(), f.anim);
		Gfx.rrect(c, px, py, pw, ph, 6, Gfx.withAlpha(track, e));
		Gfx.rrect(c, px + 2 + (pw - ph) * f.anim, py + 2, 8, 8, 4, Gfx.withAlpha(0xFFFFFFFF, e));

		if (!f.settings.isEmpty()) {
			String sign = f.expanded ? "-" : "+";
			c.drawText(textRenderer, sign, px - 10, y + 5, Gfx.withAlpha(Gfx.TEXT_DIM, e), false);
		}
		addListHit(new Hit(rx, y, rw, rh, f::toggle, f.settings.isEmpty() ? null : expand, null));
	}

	private void drawSetting(DrawContext c, Setting s, int y, int mx, int my, float e, float k, boolean interactive) {
		int rx = x0 + PAD + 6, rw = W - PAD * 2 - 16;
		if (y + s.height() < listTop || y > listBottom) return;

		if (s instanceof BoolSetting b) {
			b.anim += ((b.value ? 1f : 0f) - b.anim) * k;
			c.drawText(textRenderer, b.name, rx, y + 4, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
			int pw = 20, ph = 10;
			int px = rx + rw - pw, py = y + 3;
			Gfx.rrect(c, px, py, pw, ph, 5, Gfx.withAlpha(Gfx.lerp(Gfx.OFF, Gfx.accentB(), b.anim), e));
			Gfx.rrect(c, px + 1 + (pw - ph) * b.anim, py + 1, 8, 8, 4, Gfx.withAlpha(0xFFFFFFFF, e));
			if (interactive) addListHit(new Hit(rx - 2, y, rw + 4, s.height(), () -> b.value = !b.value, null, null));
		} else if (s instanceof NumSetting n) {
			c.drawText(textRenderer, n.name, rx, y + 3, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
			String v = n.display();
			c.drawText(textRenderer, v, rx + rw - textRenderer.getWidth(v), y + 3, Gfx.withAlpha(Gfx.TEXT, e), true);

			int ty = y + 16;
			Gfx.rrect(c, rx, ty, rw, 4, 2, Gfx.withAlpha(Gfx.OFF, e));
			float fw = Math.max(4f, rw * n.fraction());
			int fill = n.hue ? Gfx.hsb((float) (n.value / 360.0), 0.8f, 1f) : Gfx.accentB();
			Gfx.rrect(c, rx, ty, fw, 4, 2, Gfx.withAlpha(fill, e));
			Gfx.rrect(c, rx + fw - 4, ty - 2, 8, 8, 4, Gfx.withAlpha(0xFFFFFFFF, e));
			if (interactive) addListHit(new Hit(rx - 2, y + 10, rw + 4, 16, null, null, n));
		} else if (s instanceof BindSetting bs) {
			boolean ls = listening == bs;
			c.drawText(textRenderer, bs.name, rx, y + 5, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
			String label = ls ? "нажмите клавишу" : bs.display();
			int bw = Math.max(40, textRenderer.getWidth(label) + 12);
			int bx = rx + rw - bw, by = y + 1;
			Gfx.rrect(c, bx, by, bw, 14, 4, Gfx.withAlpha(ls ? Gfx.accentA() : Gfx.header(), e));
			c.drawText(textRenderer, label, bx + (bw - textRenderer.getWidth(label)) / 2, by + 3, Gfx.withAlpha(Gfx.TEXT, e), true);
			if (interactive) {
				addListHit(new Hit(bx, y, bw, 16, () -> listening = ls ? null : bs, () -> { bs.key = -1; listening = null; }, null));
			}
		}
	}

	// ------------------------------------------------------------------ Friends / Marks / Configs

	private List<Entry> entries() {
		List<Entry> out = new ArrayList<>();
		switch (tab) {
			case FRIENDS -> {
				Set<String> online = Friends.onlineCache();
				for (String n : Friends.all()) {
					boolean on = online.contains(n.toLowerCase(Locale.ROOT));
					out.add(new Entry(n, on ? "в сети" : "не в сети", on ? 0xFF4ADE80 : Gfx.OFF, null, null, () -> Friends.remove(n)));
				}
			}
			case WAYPOINTS -> {
				String dimNow = client != null && client.world != null ? client.world.getRegistryKey().getValue().getPath() : "";
				for (Waypoints.Mark m : new ArrayList<>(Waypoints.LIST)) {
					String sub = m.x + " " + m.y + " " + m.z + "  " + m.dim.replace('_', ' ');
					if (client != null && client.player != null && m.dim.equals(dimNow)) {
						double dx = m.x + 0.5 - client.player.getX(), dz = m.z + 0.5 - client.player.getZ();
						sub += "  " + (int) Math.sqrt(dx * dx + dz * dz) + " m";
					}
					out.add(new Entry(m.name, sub, m.visible ? Gfx.accentB() : Gfx.OFF,
						m.visible ? "Скрыть" : "Показать",
						() -> { m.visible = !m.visible; Waypoints.save(); },
						() -> Waypoints.remove(m)));
				}
			}
			case CONFIGS -> {
				for (String n : profileCache) {
					out.add(new Entry(n, "профиль настроек", Gfx.accentA(), "Загрузить",
						() -> setStatus(Profiles.load(n) ? "Загружено: " + n : "Не удалось загрузить"),
						() -> { Profiles.delete(n); refreshProfiles(); }));
				}
			}
			default -> {}
		}
		return out;
	}

	private String hint() {
		return switch (tab) {
			case FRIENDS -> "Ник друга";
			case WAYPOINTS -> "Название метки (рядом)";
			case CONFIGS -> "Имя профиля";
			default -> "";
		};
	}

	private String addLabel() {
		return switch (tab) {
			case CONFIGS -> "Сохр.";
			default -> "Добавить";
		};
	}

	private void submit() {
		String t = input.getText().trim();
		switch (tab) {
			case FRIENDS -> {
				if (t.isEmpty()) return;
				setStatus(Friends.add(t) ? "Друг добавлен" : "Уже в списке");
			}
			case WAYPOINTS -> {
				if (client == null || client.player == null || client.world == null) {
					setStatus("Зайдите в мир");
					return;
				}
				String name = t.isEmpty() ? "Метка " + (Waypoints.LIST.size() + 1) : t;
				Waypoints.add(name, client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ(),
					client.world.getRegistryKey().getValue().getPath());
				setStatus("Метка добавлена");
			}
			case CONFIGS -> {
				String name = Profiles.sanitize(t.isEmpty() ? "config" : t);
				Config.save();
				Profiles.save(name);
				refreshProfiles();
				setStatus("Сохранено: " + name);
			}
			default -> { return; }
		}
		input.setText("");
	}

	private void drawCustom(DrawContext c, float cy, List<Entry> entries, int mx, int my, float e, float delta) {
		int rx = x0 + PAD, rw = W - PAD * 2 - 4;
		int y = (int) cy;

		// поле ввода и кнопка
		int bw = textRenderer.getWidth(addLabel()) + 14;
		boolean visible = y + INPUT_H > listTop && y < listBottom;
		input.setVisible(visible);
		if (!visible) input.setFocused(false);
		input.setX(rx + 2);
		input.setY(y + 4);
		input.setWidth(rw - bw - 8);
		if (visible) {
			input.render(c, mx, my, delta);
			if (input.getText().isEmpty() && !input.isFocused()) {
				c.drawText(textRenderer, hint(), rx + 8, y + 7, Gfx.withAlpha(Gfx.TEXT_DIM, e), false);
			}
		}
		int bx = rx + rw - bw - 2;
		boolean hover = mx >= bx && mx < bx + bw && my >= y + 3 && my < y + 19;
		Gfx.rrect(c, bx, y + 3, bw, 16, 5, Gfx.withAlpha(hover ? Gfx.accentA() : Gfx.header(), e));
		c.drawText(textRenderer, addLabel(), bx + 7, y + 7, Gfx.withAlpha(Gfx.TEXT, e), true);
		addListHit(new Hit(bx, y + 3, bw, 16, this::submit, null, null));
		y += INPUT_H;

		if (entries.isEmpty()) {
			c.drawText(textRenderer, "Пока пусто", rx + 8, y + 4, Gfx.withAlpha(Gfx.TEXT_DIM, e), true);
			return;
		}
		for (Entry en : entries) {
			drawEntry(c, en, y, mx, my, e);
			y += ENTRY_H;
		}
	}

	private void drawEntry(DrawContext c, Entry en, int y, int mx, int my, float e) {
		int rx = x0 + PAD, rw = W - PAD * 2 - 4, rh = ENTRY_H - 2;
		if (y + rh < listTop || y > listBottom) return;

		Gfx.rrect(c, rx, y, rw, rh, 5, Gfx.withAlpha(0x30FFFFFF, e));
		Gfx.rrect(c, rx + 5, y + 8, 6, 6, 3, Gfx.withAlpha(en.dot(), e));
		c.drawText(textRenderer, en.title(), rx + 16, y + 3, Gfx.withAlpha(Gfx.TEXT, e), true);
		c.drawText(textRenderer, en.sub(), rx + 16, y + 13, Gfx.withAlpha(Gfx.TEXT_DIM, e), false);

		// кнопки справа: [действие] [x]
		int xw = 14;
		int xx = rx + rw - xw - 4, by = y + 4, bh = 14;
		boolean hx = mx >= xx && mx < xx + xw && my >= by && my < by + bh;
		Gfx.rrect(c, xx, by, xw, bh, 4, Gfx.withAlpha(hx ? 0xFFEF4444 : Gfx.header(), e));
		c.drawText(textRenderer, "x", xx + 5, by + 3, Gfx.withAlpha(Gfx.TEXT, e), true);
		addListHit(new Hit(xx, by, xw, bh, en.remove(), null, null));

		if (en.act() != null) {
			int aw = textRenderer.getWidth(en.actLabel()) + 10;
			int ax = xx - aw - 3;
			boolean ha = mx >= ax && mx < ax + aw && my >= by && my < by + bh;
			Gfx.rrect(c, ax, by, aw, bh, 4, Gfx.withAlpha(ha ? Gfx.accentA() : Gfx.header(), e));
			c.drawText(textRenderer, en.actLabel(), ax + 5, by + 3, Gfx.withAlpha(Gfx.TEXT, e), true);
			addListHit(new Hit(ax, by, aw, bh, en.act(), null, null));
		}
	}

	// ------------------------------------------------------------------ input

	private void applySlider(NumSetting n, Hit hit, double mouseX) {
		int rx = hit.x() + 2, rw = hit.w() - 4;
		double t = (mouseX - rx) / rw;
		n.set(n.min + (n.max - n.min) * Math.max(0, Math.min(1, t)));
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		BindSetting before = listening;
		for (int i = hits.size() - 1; i >= 0; i--) {
			Hit h = hits.get(i);
			if (!h.contains(mx, my)) continue;
			if (input != null) input.setFocused(false);
			if (button == 0) {
				if (h.slider() != null) {
					dragging = h.slider();
					dragHit = h;
					applySlider(dragging, dragHit, mx);
				} else if (h.left() != null) {
					h.left().run();
				}
			} else if (button == 1 && h.right() != null) {
				h.right().run();
			}
			if (before != null && listening == before) listening = null;
			return true;
		}
		// клик мимо кнопок: пусть поле ввода обработает фокус
		listening = null;
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
		if (dragging != null && dragHit != null && button == 0) {
			applySlider(dragging, dragHit, mx);
			return true;
		}
		return super.mouseDragged(mx, my, button, dx, dy);
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button) {
		if (dragging != null) {
			dragging = null;
			dragHit = null;
			Config.save();
		}
		return super.mouseReleased(mx, my, button);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
		if (mx >= x0 && mx < x0 + W && my >= listTop && my < listBottom) {
			scroll -= (float) vertical * 22f;
			return true;
		}
		return super.mouseScrolled(mx, my, horizontal, vertical);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (listening != null) {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				listening = null;
				return true;
			}
			listening.key = (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) ? -1 : keyCode;
			listening = null;
			Config.save();
			return true;
		}
		boolean typing = input != null && input.isVisible() && input.isFocused();
		if (typing) {
			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				submit();
				return true;
			}
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				input.setFocused(false);
				return true;
			}
			// Right Shift при вводе текста не закрывает меню
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
		if (keyCode == dev.legit.client.Keys.gui()) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
