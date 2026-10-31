package dev.legit.client.feature.setting;

public abstract class Setting {
	public final String name;

	protected Setting(String name) {
		this.name = name;
	}

	/** Высота строки настройки в ClickGUI. */
	public abstract int height();
}
