package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;

import java.util.concurrent.ThreadLocalRandom;

/** Снег: снежинки на экране и по желанию лёгкий снег вокруг игрока. Только визуально, на вашем экране. */
public class SnowFeature extends Feature {
	public final BoolSetting screen = add(new BoolSetting("Screen snow", true));
	public final BoolSetting inMenus = add(new BoolSetting("In menus", true));
	public final BoolSetting world = add(new BoolSetting("World snow", false));
	public final NumSetting amount = add(new NumSetting("Amount", 120, 20, 400, 10, ""));
	public final NumSetting speed = add(new NumSetting("Speed", 3, 1, 10, 1, ""));
	public final NumSetting size = add(new NumSetting("Size", 2, 1, 4, 1, ""));

	public SnowFeature() {
		super("Snow", "Falling snow on screen and around you", Category.VISUAL);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || !world.value || mc.world == null || mc.player == null) return;
		ThreadLocalRandom r = ThreadLocalRandom.current();
		int n = (int) (amount.value / 40.0) + 1;
		for (int i = 0; i < n; i++) {
			double x = mc.player.getX() + (r.nextDouble() - 0.5) * 26.0;
			double y = mc.player.getY() + 3.0 + r.nextDouble() * 7.0;
			double z = mc.player.getZ() + (r.nextDouble() - 0.5) * 26.0;
			mc.world.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, (r.nextDouble() - 0.5) * 0.02, -0.04, (r.nextDouble() - 0.5) * 0.02);
		}
	}
}
