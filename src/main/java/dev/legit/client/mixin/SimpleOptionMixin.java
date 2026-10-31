package dev.legit.client.mixin;

import dev.legit.client.feature.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> {
	@SuppressWarnings("unchecked")
	@Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
	private void legit$fullbright(CallbackInfoReturnable<T> cir) {
		if (!Features.FULLBRIGHT.enabled) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc == null || mc.options == null) return;
		if ((Object) this == mc.options.getGamma()) {
			cir.setReturnValue((T) (Double) 16.0);
		}
	}
}
