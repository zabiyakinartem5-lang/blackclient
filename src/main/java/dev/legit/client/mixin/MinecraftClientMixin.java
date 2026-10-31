package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import dev.legit.client.gui.LegitTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public ClientWorld world;

	/** Подменяем ванильное главное меню на своё. */
	@ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
	private Screen legit$title(Screen screen) {
		if (!Features.MAIN_MENU.enabled) return screen;
		if (screen instanceof TitleScreen || (screen == null && this.world == null)) {
			return new LegitTitleScreen();
		}
		return screen;
	}
}
