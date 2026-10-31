package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import dev.legit.client.gui.CrosshairRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	/** Заменяем ванильный прицел своим. */
	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void legit$crosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (!Features.CROSSHAIR.enabled) return;
		CrosshairRenderer.draw(context);
		ci.cancel();
	}
}
