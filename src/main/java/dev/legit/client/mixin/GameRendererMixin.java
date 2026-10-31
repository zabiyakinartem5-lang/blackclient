package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	/** Zoom и смещение FOV. */
	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	private void legit$fov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
		float fov = cir.getReturnValueF();
		boolean changed = false;

		if (Features.FOV.enabled) {
			fov = Math.max(10f, fov + (float) Features.FOV.offset.value);
			changed = true;
		}
		float zoom = Features.ZOOM.currentFactor();
		if (zoom > 1.0001f) {
			fov /= zoom;
			changed = true;
		}
		if (changed) cir.setReturnValue(fov);
	}

	/**
	 * Aspect Ratio: у перспективной матрицы m00 = 1 / (aspect * tan(fov / 2)).
	 * Подменяем реальный aspect окна на выбранный, домножив m00 на real / target.
	 */
	@Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"))
	private void legit$aspect(CallbackInfoReturnable<Matrix4f> cir) {
		if (!Features.ASPECT.enabled) return;
		var window = MinecraftClient.getInstance().getWindow();
		float real = (float) window.getFramebufferWidth() / (float) window.getFramebufferHeight();
		float target = (float) Features.ASPECT.ratio.value;
		Matrix4f m = cir.getReturnValue();
		m.m00(m.m00() * real / target);
	}

	/** No Hurt Cam: пропускаем тряску камеры при уроне. */
	@Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
	private void legit$noHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		if (Features.NO_HURT_CAM.enabled) ci.cancel();
	}
}
