package dev.legit.client.gui.hud;

import dev.legit.client.feature.Features;

import java.util.List;

public final class HudElements {
	public static final HudElement INFO = new HudElement("info", "Info", HudElement.LEFT, 0.008f, 0.012f, 90, 40,
		() -> Features.HUD.enabled, () -> Features.HUD.scale.value);

	public static final HudElement ARMOR = new HudElement("armor", "Armor", HudElement.RIGHT, 0.992f, 0.38f, 52, 88,
		() -> Features.HUD.enabled && Features.HUD.armor.value, () -> Features.HUD.scale.value);

	public static final HudElement EFFECTS = new HudElement("effects", "Effects", HudElement.LEFT, 0.008f, 0.26f, 80, 15,
		() -> Features.EFFECTS.enabled, () -> Features.EFFECTS.scale.value);

	public static final HudElement KEYSTROKES = new HudElement("keys", "Keystrokes", HudElement.LEFT, 0.008f, 0.62f, 70, 90,
		() -> Features.KEYSTROKES.enabled, () -> Features.KEYSTROKES.scale.value);

	public static final HudElement MODULE_LIST = new HudElement("modules", "Module List", HudElement.RIGHT, 0.992f, 0.012f, 70, 40,
		() -> Features.MODULE_LIST.enabled, () -> Features.MODULE_LIST.scale.value);

	public static final HudElement WATERMARK = new HudElement("watermark", "Watermark", HudElement.CENTER, 0.5f, 0.012f, 110, 16,
		() -> Features.WATERMARK.enabled, () -> 1.0);

	public static final HudElement TARGET = new HudElement("target", "Target HUD", HudElement.CENTER, 0.5f, 0.55f, 120, 38,
		() -> Features.TARGET_HUD.enabled, () -> Features.TARGET_HUD.scale.value);

	public static final HudElement COMBO = new HudElement("combo", "Combo", HudElement.CENTER, 0.5f, 0.47f, 70, 24,
		() -> Features.COMBO.enabled, () -> Features.COMBO.scale.value);

	public static final HudElement ITEMS = new HudElement("items", "PvP Items", HudElement.LEFT, 0.008f, 0.50f, 44, 60,
		() -> Features.PVP_ITEMS.enabled, () -> Features.PVP_ITEMS.scale.value);

	public static final HudElement STATUS = new HudElement("status", "Move Status", HudElement.LEFT, 0.008f, 0.44f, 70, 14,
		() -> Features.STATUS.enabled, () -> Features.STATUS.scale.value);

	public static final HudElement DEATH = new HudElement("death", "Death Point", HudElement.LEFT, 0.008f, 0.36f, 110, 26,
		() -> Features.DEATH_POINT.enabled, () -> Features.DEATH_POINT.scale.value);

	public static final HudElement INVENTORY = new HudElement("inventory", "Inventory HUD", HudElement.CENTER, 0.5f, 0.64f, 170, 62,
		() -> Features.INVENTORY_HUD.enabled, () -> Features.INVENTORY_HUD.scale.value);

	public static final HudElement STAFF = new HudElement("staff", "Staff List", HudElement.RIGHT, 0.86f, 0.22f, 90, 28,
		() -> Features.STAFF_LIST.enabled, () -> Features.STAFF_LIST.scale.value);

	public static final HudElement FRIENDS = new HudElement("friends", "Friends", HudElement.RIGHT, 0.86f, 0.60f, 90, 28,
		() -> Features.FRIENDS.enabled && Features.FRIENDS.hudList.value, () -> Features.FRIENDS.scale.value);

	public static final HudElement MARKS = new HudElement("marks", "Marks", HudElement.CENTER, 0.5f, 0.08f, 110, 28,
		() -> Features.WAYPOINTS.enabled, () -> Features.WAYPOINTS.scale.value);

	public static final List<HudElement> ALL = List.of(
		INFO, ARMOR, EFFECTS, KEYSTROKES, MODULE_LIST, WATERMARK, TARGET, COMBO, ITEMS, STATUS, DEATH, INVENTORY, STAFF, FRIENDS, MARKS
	);

	private HudElements() {}

	public static void resetAll() {
		for (HudElement e : ALL) e.reset();
	}
}
