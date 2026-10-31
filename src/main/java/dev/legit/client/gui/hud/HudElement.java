package dev.legit.client.gui.hud;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/** Один перетаскиваемый блок HUD. Позиция хранится в долях экрана (0..1), поэтому не ломается при смене размера окна. */
public class HudElement {
	public static final int LEFT = 0, RIGHT = 1, CENTER = 2;

	public final String id;
	public final String label;
	public final int anchor;
	public final float defX, defY;
	public final BooleanSupplier visible;
	public final DoubleSupplier scaleOf;

	/** Позиция якоря (доли экрана). */
	public float fx, fy;
	/** Размер содержимого с прошлого кадра (до масштабирования). */
	public int w, h;
	/** Масштаб, использованный в прошлом кадре. */
	public float scale = 1f;

	public HudElement(String id, String label, int anchor, float defX, float defY, int defW, int defH,
					  BooleanSupplier visible, DoubleSupplier scaleOf) {
		this.id = id;
		this.label = label;
		this.anchor = anchor;
		this.defX = defX;
		this.defY = defY;
		this.fx = defX;
		this.fy = defY;
		this.w = defW;
		this.h = defH;
		this.visible = visible;
		this.scaleOf = scaleOf;
	}

	public float left(int screenW) {
		float p = fx * screenW;
		float ww = w * scale;
		return switch (anchor) {
			case RIGHT -> p - ww;
			case CENTER -> p - ww / 2f;
			default -> p;
		};
	}

	public float top(int screenH) {
		return fy * screenH;
	}

	public float anchorOffset() {
		float ww = w * scale;
		return switch (anchor) {
			case RIGHT -> ww;
			case CENTER -> ww / 2f;
			default -> 0f;
		};
	}

	public void reset() {
		fx = defX;
		fy = defY;
	}
}
