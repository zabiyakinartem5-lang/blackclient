package dev.legit.client.mixin;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraInvoker {
	@Invoker("setRotation")
	void legit$setRotation(float yaw, float pitch);
}
