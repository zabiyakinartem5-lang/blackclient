package dev.legit.client.pvp;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/** Статистика боя только для показа на экране: цель, комбо, расстояние удара. Ничего не меняет в игре. */
public final class PvpStats {
	public static LivingEntity target;
	public static long targetTime;
	public static int combo;
	public static long lastHitMs;
	public static double reach = -1;

	private static float lastHealth = -1f;

	private PvpStats() {}

	public static void onAttack(PlayerEntity player, Entity entity) {
		if (!(entity instanceof LivingEntity living)) return;
		long now = System.currentTimeMillis();

		target = living;
		targetTime = now;
		reach = reachTo(player, living);

		// не считаем чаще, чем раз в 350 мс (кулдаун атаки), чтобы не накручивать комбо кликами
		if (now - lastHitMs >= 350) {
			combo = (now - lastHitMs > 3000) ? 1 : combo + 1;
			lastHitMs = now;
		}
	}

	public static void tick(MinecraftClient mc) {
		if (mc.player == null) {
			lastHealth = -1f;
			return;
		}
		float hp = mc.player.getHealth();
		if (lastHealth >= 0 && hp < lastHealth - 0.01f) combo = 0; // вас ударили - комбо сброшено
		lastHealth = hp;
		if (combo > 0 && System.currentTimeMillis() - lastHitMs > 3000) combo = 0;
	}

	/** Расстояние от глаз до ближайшей точки хитбокса цели. */
	private static double reachTo(PlayerEntity p, Entity e) {
		Vec3d eye = p.getEyePos();
		Box b = e.getBoundingBox();
		double x = Math.max(b.minX, Math.min(eye.x, b.maxX));
		double y = Math.max(b.minY, Math.min(eye.y, b.maxY));
		double z = Math.max(b.minZ, Math.min(eye.z, b.maxZ));
		return eye.distanceTo(new Vec3d(x, y, z));
	}
}
