package dev.legit.client.feature.impl;

import dev.legit.client.data.Friends;
import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;

/** Друзья: список в сети на экране и уведомления о входе/выходе. */
public class FriendsFeature extends Feature {
	public final BoolSetting hudList = add(new BoolSetting("HUD list", true));
	public final BoolSetting alerts = add(new BoolSetting("Join/leave alerts", true));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	private int timer;
	private Set<String> prev;

	public FriendsFeature() {
		super("Friends", "Show friends who are online and alert when they join", Category.FRIENDS);
		this.enabled = true;
		this.anim = 1f;
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (mc.player == null || mc.getNetworkHandler() == null) {
			prev = null;
			Friends.clearOnline();
			return;
		}
		if (++timer < 10) return;
		timer = 0;

		Set<String> now = Friends.refresh(mc);
		if (enabled && alerts.value && prev != null) {
			for (String n : now) if (!prev.contains(n)) notify(mc, Friends.display(n), true);
			for (String n : prev) if (!now.contains(n)) notify(mc, Friends.display(n), false);
		}
		prev = new HashSet<>(now);
	}

	private static void notify(MinecraftClient mc, String name, boolean joined) {
		mc.player.sendMessage(Text.literal("[Friends] ").formatted(Formatting.GREEN)
			.append(Text.literal(name + (joined ? " зашёл на сервер" : " вышел с сервера")).formatted(Formatting.GRAY)), false);
	}
}
