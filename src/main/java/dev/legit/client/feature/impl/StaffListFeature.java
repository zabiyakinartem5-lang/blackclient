package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Показывает игроков из списка (Tab), у которых в префиксе есть пометка персонала: admin, mod, helper и т.п.
 * Использует только то, что сервер и так показывает всем в Tab.
 */
public class StaffListFeature extends Feature {
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	private static final String[] KEYWORDS = {
		"owner", "admin", "mod", "helper", "staff", "support", "dev", "manager", "curator",
		"владел", "админ", "модер", "хелп", "стафф", "саппорт", "куратор", "основател"
	};

	/** Готовые строки вида "[Admin] Name". Обновляются раз в секунду. */
	public static volatile List<String> staff = List.of();

	private int timer;

	public StaffListFeature() {
		super("Staff List", "Staff members online (from the Tab list)", Category.HUD);
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!enabled || mc.getNetworkHandler() == null) {
			staff = List.of();
			return;
		}
		if (++timer < 20) return;
		timer = 0;

		List<String> found = new ArrayList<>();
		for (PlayerListEntry e : mc.getNetworkHandler().getPlayerList()) {
			String name = e.getProfile().getName();
			String tag = "";
			Team team = e.getScoreboardTeam();
			if (team != null) tag += team.getPrefix().getString();
			if (e.getDisplayName() != null) tag += e.getDisplayName().getString();
			tag = tag.replace(name, "").trim();
			if (tag.isEmpty()) continue;

			String low = tag.toLowerCase(Locale.ROOT);
			for (String k : KEYWORDS) {
				if (low.contains(k)) {
					found.add(tag + " " + name);
					break;
				}
			}
		}
		staff = found;
	}
}
