package dev.legit.client.feature.setting;

public class BoolSetting extends Setting {
	public boolean value;
	/** Анимация переключателя 0..1 (для GUI). */
	public float anim;

	public BoolSetting(String name, boolean value) {
		super(name);
		this.value = value;
		this.anim = value ? 1f : 0f;
	}

	@Override
	public int height() {
		return 16;
	}
}
