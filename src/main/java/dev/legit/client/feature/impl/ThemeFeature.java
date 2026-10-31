package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import dev.legit.client.gui.Gfx;

/** Цвета и вид интерфейса: два акцента, насыщенность, радуга, прозрачность и закругление углов у ВСЕХ элементов. */
public class ThemeFeature extends Feature {
	public final NumSetting hueA = add(new NumSetting("Color 1", 262, 0, 360, 1, "\u00b0").asHue());
	public final NumSetting hueB = add(new NumSetting("Color 2", 188, 0, 360, 1, "\u00b0").asHue());
	public final NumSetting saturation = add(new NumSetting("Saturation", 75, 0, 100, 1, "%"));
	public final NumSetting brightness = add(new NumSetting("Brightness", 96, 40, 100, 1, "%"));
	public final BoolSetting rainbow = add(new BoolSetting("Rainbow", false));
	public final NumSetting rainbowSpeed = add(new NumSetting("Rainbow speed", 3, 1, 10, 1, ""));
	public final NumSetting opacity = add(new NumSetting("Menu opacity", 94, 20, 100, 1, "%"));
	public final NumSetting hudOpacity = add(new NumSetting("HUD opacity", 69, 0, 100, 1, "%"));
	public final NumSetting radius = add(new NumSetting("Corner radius", 8, 0, 16, 1, ""));

	public ThemeFeature() {
		super("Theme", "Colors, transparency and rounded corners", Category.CLIENT);
		noBind();
		this.enabled = true;
		this.anim = 1f;
		this.alwaysOn = true;
	}

	private float shift() {
		if (!rainbow.value) return 0f;
		double t = (System.currentTimeMillis() % 100_000L) / 1000.0;
		return (float) ((t * rainbowSpeed.value * 12.0) % 360.0);
	}

	public int colorA() {
		float h = (float) ((hueA.value + shift()) / 360.0);
		return Gfx.hsb(h, (float) saturation.value / 100f, (float) brightness.value / 100f);
	}

	public int colorB() {
		float h = (float) ((hueB.value + shift()) / 360.0);
		return Gfx.hsb(h, (float) saturation.value / 100f, (float) brightness.value / 100f);
	}

	public int panelAlpha() {
		return Math.max(0, Math.min(255, (int) (opacity.value / 100.0 * 255.0)));
	}

	public int hudAlpha() {
		return Math.max(0, Math.min(255, (int) (hudOpacity.value / 100.0 * 255.0)));
	}

	/** Множитель радиуса скругления: 8 = как задумано, 0 = прямые углы. */
	public float radiusScale() {
		return (float) (radius.value / 8.0);
	}
}
