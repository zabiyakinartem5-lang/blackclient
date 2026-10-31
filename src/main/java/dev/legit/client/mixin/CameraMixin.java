package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {
	// Подменяем первый вызов setRotation в update(): камера считает позицию уже с нашими углами.
	@Redirect(
		method = "update",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0)
	)
	private void legit$freelookRotation(Camera camera, float yaw, float pitch) {
		if (Features.FREELOOK.isLooking()) {
			((CameraInvoker) camera).legit$setRotation(Features.FREELOOK.yaw, Features.FREELOOK.pitch);
		} else {
			((CameraInvoker) camera).legit$setRotation(yaw, pitch);
		}
	}
}
