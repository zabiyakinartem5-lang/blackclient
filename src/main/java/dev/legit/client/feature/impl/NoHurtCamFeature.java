package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;

/** Убирает тряску камеры при получении урона (только визуально). */
public class NoHurtCamFeature extends Feature {
	public NoHurtCamFeature() {
		super("No Hurt Cam", "Disable camera shake when you take damage", Category.VISUAL);
	}
}
