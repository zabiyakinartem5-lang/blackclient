package dev.legit.client.feature.setting;

import net.minecraft.client.util.InputUtil;

/** Клавиша, назначаемая прямо в меню клиента. key <= 0 означает «не назначена». */
public class BindSetting extends Setting {
	public int key;

	public BindSetting(String name, int key) {
		super(name);
		this.key = key;
	}

	public String display() {
		if (key <= 0) return "None";
		try {
			return InputUtil.fromKeyCode(key, 0).getLocalizedText().getString();
		} catch (Exception e) {
			return "Key " + key;
		}
	}

	@Override
	public int height() {
		return 17;
	}
}
