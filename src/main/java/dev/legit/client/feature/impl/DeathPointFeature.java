package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;

/** Запоминает, где вы умерли в последний раз, и показывает координаты и расстояние до этого места. */
public class DeathPointFeature extends Feature {
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public static boolean has;
	public static int x, y, z;
	public static String dim = "";

	private boolean wasDead;

	public DeathPointFeature() {
		super("Death Point", "Coordinates of your last death", Category.HUD);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || mc.player == null || mc.world == null) {
			wasDead = false;
			return;
		}
		boolean dead = mc.player.isDead() || mc.player.getHealth() <= 0f;
		if (dead && !wasDead) {
			x = mc.player.getBlockX();
			y = mc.player.getBlockY();
			z = mc.player.getBlockZ();
			dim = mc.world.getRegistryKey().getValue().getPath();
			has = true;
		}
		wasDead = dead;
	}
}
