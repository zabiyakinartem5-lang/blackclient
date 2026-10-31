package dev.legit.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.legit.client.LegitClient;
import dev.legit.client.feature.Features;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

/**
 * Motion blur через накопление кадров: к свежему кадру подмешивается предыдущий результат.
 * Вызывается после отрисовки мира, до интерфейса, поэтому HUD и меню не размываются.
 */
public final class MotionBlurRenderer {
	private static Framebuffer history;
	private static long last = System.nanoTime();

	private MotionBlurRenderer() {}

	public static void apply(MinecraftClient mc) {
		if (!Features.MOTION_BLUR.enabled) {
			release();
			return;
		}
		try {
			Framebuffer main = mc.getFramebuffer();
			int w = main.textureWidth, h = main.textureHeight;

			if (history == null || history.textureWidth != w || history.textureHeight != h) {
				release();
				history = new SimpleFramebuffer(w, h, false);
				copy(main, w, h);
				last = System.nanoTime();
				return;
			}

			long now = System.nanoTime();
			float dt = Math.min(0.1f, (now - last) / 1_000_000_000f);
			last = now;

			// вес предыдущего кадра не зависит от FPS: strength^(dt*60)
			float weight = (float) Math.pow(Features.MOTION_BLUR.strength.value / 100.0, dt * 60.0);

			// 1) подмешать историю поверх текущего кадра
			main.beginWrite(false);
			GlStateManager._enableBlend();
			GL14.glBlendColor(0f, 0f, 0f, weight);
			GlStateManager._blendFuncSeparate(GL14.GL_CONSTANT_ALPHA, GL14.GL_ONE_MINUS_CONSTANT_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			history.draw(w, h);
			GlStateManager._disableBlend();
			RenderSystem.defaultBlendFunc();

			// 2) запомнить результат для следующего кадра
			copy(main, w, h);
		} catch (Throwable t) {
			LegitClient.LOG.error("Motion Blur сломался и был отключён", t);
			Features.MOTION_BLUR.enabled = false;
			release();
		}
	}

	private static void copy(Framebuffer main, int w, int h) {
		history.beginWrite(false);
		GlStateManager._disableBlend();
		main.draw(w, h);
		main.beginWrite(false);
	}

	private static void release() {
		if (history != null) {
			history.delete();
			history = null;
		}
	}
}
