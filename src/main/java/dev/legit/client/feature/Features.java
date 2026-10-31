package dev.legit.client.feature;

import dev.legit.client.feature.impl.*;

import java.util.List;

public final class Features {
	// Visual
	public static final ZoomFeature ZOOM = new ZoomFeature();
	public static final FullbrightFeature FULLBRIGHT = new FullbrightFeature();
	public static final FreelookFeature FREELOOK = new FreelookFeature();
	public static final AspectRatioFeature ASPECT = new AspectRatioFeature();
	public static final MotionBlurFeature MOTION_BLUR = new MotionBlurFeature();
	public static final FovFeature FOV = new FovFeature();
	public static final NoHurtCamFeature NO_HURT_CAM = new NoHurtCamFeature();
	public static final CrosshairFeature CROSSHAIR = new CrosshairFeature();
	public static final StarrySkyFeature STARRY_SKY = new StarrySkyFeature();
	public static final SnowFeature SNOW = new SnowFeature();
	public static final AtmosphereFeature ATMOSPHERE = new AtmosphereFeature();
	public static final TimeChangerFeature TIME_CHANGER = new TimeChangerFeature();

	// Movement
	public static final ToggleSprintFeature TOGGLE_SPRINT = new ToggleSprintFeature();
	public static final ToggleSneakFeature TOGGLE_SNEAK = new ToggleSneakFeature();

	// HUD
	public static final HudFeature HUD = new HudFeature();
	public static final KeystrokesFeature KEYSTROKES = new KeystrokesFeature();
	public static final EffectsFeature EFFECTS = new EffectsFeature();
	public static final ModuleListFeature MODULE_LIST = new ModuleListFeature();
	public static final WatermarkFeature WATERMARK = new WatermarkFeature();
	public static final StatusFeature STATUS = new StatusFeature();
	public static final DeathPointFeature DEATH_POINT = new DeathPointFeature();
	public static final ChatTimeFeature CHAT_TIME = new ChatTimeFeature();

	public static final StaffListFeature STAFF_LIST = new StaffListFeature();
	public static final InventoryHudFeature INVENTORY_HUD = new InventoryHudFeature();

	// Friends / Marks
	public static final FriendsFeature FRIENDS = new FriendsFeature();
	public static final WaypointsFeature WAYPOINTS = new WaypointsFeature();

	// PvP (только информация, без изменения игровой механики)
	public static final TargetHudFeature TARGET_HUD = new TargetHudFeature();
	public static final ComboFeature COMBO = new ComboFeature();
	public static final PvpItemsFeature PVP_ITEMS = new PvpItemsFeature();

	// Client
	public static final ThemeFeature THEME = new ThemeFeature();
	public static final MainMenuFeature MAIN_MENU = new MainMenuFeature();
	public static final KeybindsFeature KEYBINDS = new KeybindsFeature();

	public static final List<Feature> ALL = List.of(
		ZOOM, FULLBRIGHT, FREELOOK, ASPECT, MOTION_BLUR, FOV, NO_HURT_CAM, CROSSHAIR, STARRY_SKY, SNOW, ATMOSPHERE, TIME_CHANGER,
		TOGGLE_SPRINT, TOGGLE_SNEAK,
		HUD, KEYSTROKES, EFFECTS, MODULE_LIST, WATERMARK, STATUS, DEATH_POINT, CHAT_TIME, STAFF_LIST, INVENTORY_HUD,
		FRIENDS, WAYPOINTS,
		TARGET_HUD, COMBO, PVP_ITEMS,
		KEYBINDS, THEME, MAIN_MENU
	);

	private Features() {}
}
