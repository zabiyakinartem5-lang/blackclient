package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	private void legit$freelook(double dx, double dy, CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc != null && mc.player == (Object) this && Features.FREELOOK.isLooking()) {
			Features.FREELOOK.rotate(dx, dy);
			ci.cancel();
		}
	}
}
