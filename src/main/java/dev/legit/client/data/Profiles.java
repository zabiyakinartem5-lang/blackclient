package dev.legit.client.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.legit.client.Config;
import dev.legit.client.LegitClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/** Именованные профили настроек в config/legitclient_profiles/. */
public final class Profiles {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path DIR = FabricLoader.getInstance().getConfigDir().resolve("legitclient_profiles");

	private Profiles() {}

	public static String sanitize(String name) {
		String s = name.trim().replaceAll("[^\\p{L}\\p{N}_\\- ]", "_");
		if (s.length() > 24) s = s.substring(0, 24);
		return s.isBlank() ? "config" : s;
	}

	public static List<String> list() {
		List<String> out = new ArrayList<>();
		if (!Files.isDirectory(DIR)) return out;
		try (Stream<Path> st = Files.list(DIR)) {
			st.forEach(p -> {
				String n = p.getFileName().toString();
				if (n.endsWith(".json")) out.add(n.substring(0, n.length() - 5));
			});
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось прочитать профили", e);
		}
		Collections.sort(out, String.CASE_INSENSITIVE_ORDER);
		return out;
	}

	public static void save(String name) {
		try {
			Files.createDirectories(DIR);
			Files.writeString(DIR.resolve(sanitize(name) + ".json"), GSON.toJson(Config.snapshot()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось сохранить профиль", e);
		}
	}

	public static boolean load(String name) {
		Path p = DIR.resolve(sanitize(name) + ".json");
		if (!Files.exists(p)) return false;
		try {
			Config.apply(GSON.fromJson(Files.readString(p, StandardCharsets.UTF_8), JsonObject.class));
			Config.save();
			return true;
		} catch (Exception e) {
			LegitClient.LOG.warn("Не удалось загрузить профиль", e);
			return false;
		}
	}

	public static void delete(String name) {
		try {
			Files.deleteIfExists(DIR.resolve(sanitize(name) + ".json"));
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось удалить профиль", e);
		}
	}
}
