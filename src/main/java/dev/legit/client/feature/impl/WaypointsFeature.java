package dev.legit.client.feature.impl;

import dev.legit.client.Keys;
import dev.legit.client.data.Waypoints;
import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Метки: ближайшие точки на экране с расстоянием и направлением. Клавиша добавления назначается в Client → Keybinds. */
public class WaypointsFeature extends Feature {
	public final BoolSetting sameDim = add(new BoolSetting("Same dimension only", true));
	public final NumSetting maxShown = add(new NumSetting("Max shown", 5, 1, 10, 1, ""));
	public final NumSetting scale = add(new NumSetting("Scale", 1.0, 0.7, 1.6, 0.1, "x"));

	public WaypointsFeature() {
		super("Marks", "Show nearest marks with distance and direction", Category.WAYPOINTS);
		this.enabled = true;
		this.anim = 1f;
	}

	@Override
	public void onTick(MinecraftClient mc) {
		if (!Keys.pressedOnce(Keys.mark())) return;
		if (!enabled || mc.player == null || mc.world == null || mc.currentScreen != null) return;

		String dim = mc.world.getRegistryKey().getValue().getPath();
		String name = "Метка " + (Waypoints.LIST.size() + 1);
		Waypoints.add(name, mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ(), dim);
		mc.player.sendMessage(Text.literal("[Marks] ").formatted(Formatting.AQUA)
			.append(Text.literal("добавлена «" + name + "»").formatted(Formatting.GRAY)), false);
	}
}
