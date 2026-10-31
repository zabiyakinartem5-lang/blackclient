package dev.legit.client;

import dev.legit.client.data.Friends;
import dev.legit.client.data.Waypoints;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.Features;
import dev.legit.client.gui.ClickGui;
import dev.legit.client.gui.HudRenderer;
import dev.legit.client.render.MotionBlurRenderer;
import net.fabricmc.api.ClientModInitializer;
import dev.legit.client.pvp.PvpStats;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegitClient implements ClientModInitializer {
	public static final Logger LOG = LoggerFactory.getLogger("legitclient");

	@Override
	public void onInitializeClient() {
		Config.load();
		Friends.load();
		Waypoints.load();

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			if (Keys.pressedOnce(Keys.gui()) && mc.currentScreen == null) mc.setScreen(new ClickGui());
			for (Feature f : Features.ALL) {
				// клавиша модуля, назначенная в меню
				if (!f.alwaysOn && f.bind.key > 0 && Keys.pressedOnce(f.bind.key) && mc.currentScreen == null && mc.player != null) {
					f.toggle();
				}
				f.onTick(mc);
			}
			PvpStats.tick(mc);
		});

		HudRenderCallback.EVENT.register((context, tickCounter) -> {
			Features.ZOOM.update();
			HudRenderer.render(context, MinecraftClient.getInstance());
		});

		// только запоминаем удар для статистики; сам удар не меняем и не отменяем
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
			if (world.isClient) PvpStats.onAttack(player, entity);
			return ActionResult.PASS;
		});

		WorldRenderEvents.END.register(context -> MotionBlurRenderer.apply(MinecraftClient.getInstance()));

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());

		LOG.info("Legit Client загружен. Меню: Right Shift.");
	}
}
