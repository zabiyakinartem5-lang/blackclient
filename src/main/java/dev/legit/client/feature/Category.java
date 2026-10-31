package dev.legit.client.feature;

public enum Category {
	VISUAL("Visual"),
	MOVEMENT("Movement"),
	HUD("HUD"),
	PVP("PvP"),
	CLIENT("Client"),
	FRIENDS("Friends"),
	WAYPOINTS("Marks"),
	CONFIGS("Configs");

	public final String label;

	Category(String label) {
		this.label = label;
	}

	/** Вкладки со своим содержимым (поле ввода и список записей). */
	public boolean isCustom() {
		return this == FRIENDS || this == WAYPOINTS || this == CONFIGS;
	}
}
