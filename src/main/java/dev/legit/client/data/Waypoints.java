package dev.legit.client.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.legit.client.LegitClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Метки (координаты с названием). Хранятся в config/legitclient_marks.json. */
public final class Waypoints {
	public static final class Mark {
		public String name = "";
		public int x, y, z;
		public String dim = "overworld";
		public boolean visible = true;
	}

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("legitclient_marks.json");

	public static final List<Mark> LIST = new ArrayList<>();

	private Waypoints() {}

	public static void load() {
		LIST.clear();
		if (!Files.exists(FILE)) return;
		try {
			Mark[] arr = GSON.fromJson(Files.readString(FILE, StandardCharsets.UTF_8), Mark[].class);
			if (arr != null) for (Mark m : arr) if (m != null && m.name != null) LIST.add(m);
		} catch (Exception e) {
			LegitClient.LOG.warn("Не удалось прочитать метки", e);
		}
	}

	public static void save() {
		try {
			Files.writeString(FILE, GSON.toJson(LIST), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось сохранить метки", e);
		}
	}

	public static Mark add(String name, int x, int y, int z, String dim) {
		Mark m = new Mark();
		m.name = name;
		m.x = x;
		m.y = y;
		m.z = z;
		m.dim = dim;
		LIST.add(m);
		save();
		return m;
	}

	public static void remove(Mark m) {
		LIST.remove(m);
		save();
	}
}
