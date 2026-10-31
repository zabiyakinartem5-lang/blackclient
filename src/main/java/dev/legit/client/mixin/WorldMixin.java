package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
	/** Яркие звёзды в Starry Sky. require = 0: если метод в этой версии называется иначе, мод просто не применяет эффект. */
	@Inject(method = "getStarBrightness", at = @At("RETURN"), cancellable = true, require = 0)
	private void legit$stars(float tickDelta, CallbackInfoReturnable<Float> cir) {
		if (Features.STARRY_SKY.enabled && Features.STARRY_SKY.brightStars.value && (Object) this instanceof ClientWorld) {
			cir.setReturnValue(1.0f);
		}
	}
}
