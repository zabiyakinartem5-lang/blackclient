package dev.legit.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.Features;
import dev.legit.client.feature.setting.BindSetting;
import dev.legit.client.feature.setting.BoolSetting;
import dev.legit.client.feature.setting.NumSetting;
import dev.legit.client.feature.setting.Setting;
import dev.legit.client.gui.hud.HudElement;
import dev.legit.client.gui.hud.HudElements;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("legitclient.json");

	private Config() {}

	/** Все настройки модулей и позиции блоков HUD в виде JSON (используется и для профилей). */
	public static JsonObject snapshot() {
		JsonObject root = new JsonObject();
		for (Feature f : Features.ALL) {
			JsonObject fo = new JsonObject();
			fo.addProperty("enabled", f.enabled);
			JsonObject so = new JsonObject();
			for (Setting s : f.settings) {
				if (s instanceof BoolSetting b) so.addProperty(s.name, b.value);
				else if (s instanceof NumSetting n) so.addProperty(s.name, n.value);
				else if (s instanceof BindSetting k) so.addProperty(s.name, k.key);
			}
			fo.add("settings", so);
			root.add(f.name, fo);
		}
		JsonObject pos = new JsonObject();
		for (HudElement e : HudElements.ALL) {
			JsonObject po = new JsonObject();
			po.addProperty("x", e.fx);
			po.addProperty("y", e.fy);
			pos.add(e.id, po);
		}
		root.add("_hud", pos);
		return root;
	}

	/** Применяет JSON-снимок. Незнакомые и отсутствующие поля пропускаются. */
	public static void apply(JsonObject root) {
		if (root == null) return;
		if (root.has("_hud")) {
			JsonObject pos = root.getAsJsonObject("_hud");
			for (HudElement e : HudElements.ALL) {
				if (!pos.has(e.id)) continue;
				JsonObject po = pos.getAsJsonObject(e.id);
				if (po.has("x")) e.fx = Math.max(0f, Math.min(1f, po.get("x").getAsFloat()));
				if (po.has("y")) e.fy = Math.max(0f, Math.min(1f, po.get("y").getAsFloat()));
			}
		}
		for (Feature f : Features.ALL) {
			if (!root.has(f.name)) continue;
			JsonObject fo = root.getAsJsonObject(f.name);
			if (fo.has("enabled")) {
				boolean en = fo.get("enabled").getAsBoolean();
				if (en != f.enabled) f.setEnabled(en);
				f.anim = en ? 1f : 0f;
			}
			if (!fo.has("settings")) continue;
			JsonObject so = fo.getAsJsonObject("settings");
			for (Setting s : f.settings) {
				if (!so.has(s.name)) continue;
				if (s instanceof BoolSetting b) {
					b.value = so.get(s.name).getAsBoolean();
					b.anim = b.value ? 1f : 0f;
				} else if (s instanceof NumSetting n) {
					n.set(so.get(s.name).getAsDouble());
				} else if (s instanceof BindSetting k) {
					k.key = so.get(s.name).getAsInt();
				}
			}
		}
	}

	public static void save() {
		try {
			Files.writeString(FILE, GSON.toJson(snapshot()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось сохранить конфиг", e);
		}
	}

	public static void load() {
		if (!Files.exists(FILE)) return;
		try {
			apply(GSON.fromJson(Files.readString(FILE, StandardCharsets.UTF_8), JsonObject.class));
		} catch (Exception e) {
			LegitClient.LOG.warn("Не удалось прочитать конфиг, используются значения по умолчанию", e);
		}
	}
}
