package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
	private void legit$zoomScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (vertical != 0 && Features.ZOOM.active()) {
			Features.ZOOM.scroll(vertical);
			ci.cancel();
		}
	}
}
