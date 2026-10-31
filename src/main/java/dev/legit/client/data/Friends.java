package dev.legit.client.data;

import com.google.gson.Gson;
import dev.legit.client.LegitClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Список друзей (имена игроков). Хранится в config/legitclient_friends.json. */
public final class Friends {
	private static final Gson GSON = new Gson();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("legitclient_friends.json");

	/** lower-case имя -> имя как ввёл пользователь. */
	private static final Map<String, String> NAMES = new LinkedHashMap<>();
	private static volatile Set<String> onlineCache = Set.of();

	private Friends() {}

	public static void load() {
		NAMES.clear();
		if (!Files.exists(FILE)) return;
		try {
			String[] arr = GSON.fromJson(Files.readString(FILE, StandardCharsets.UTF_8), String[].class);
			if (arr != null) for (String n : arr) if (n != null && !n.isBlank()) NAMES.put(n.toLowerCase(Locale.ROOT), n);
		} catch (Exception e) {
			LegitClient.LOG.warn("Не удалось прочитать список друзей", e);
		}
	}

	public static void save() {
		try {
			Files.writeString(FILE, GSON.toJson(NAMES.values().toArray(new String[0])), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LegitClient.LOG.warn("Не удалось сохранить список друзей", e);
		}
	}

	/** @return false, если такой друг уже есть. */
	public static boolean add(String name) {
		String key = name.toLowerCase(Locale.ROOT);
		if (NAMES.containsKey(key)) return false;
		NAMES.put(key, name);
		save();
		return true;
	}

	public static void remove(String name) {
		NAMES.remove(name.toLowerCase(Locale.ROOT));
		save();
	}

	public static boolean has(String name) {
		return NAMES.containsKey(name.toLowerCase(Locale.ROOT));
	}

	public static List<String> all() {
		return new ArrayList<>(NAMES.values());
	}

	/** Друзья, которые сейчас в сети (lower-case). Обновляется FriendsFeature. */
	public static Set<String> onlineCache() {
		return onlineCache;
	}

	/** Пересчитывает, какие друзья есть в списке игроков сервера. */
	public static Set<String> refresh(MinecraftClient mc) {
		Set<String> now = new HashSet<>();
		if (mc.getNetworkHandler() != null) {
			for (PlayerListEntry e : mc.getNetworkHandler().getPlayerList()) {
				String n = e.getProfile().getName().toLowerCase(Locale.ROOT);
				if (NAMES.containsKey(n)) now.add(n);
			}
		}
		onlineCache = now;
		return now;
	}

	public static void clearOnline() {
		onlineCache = Set.of();
	}

	public static String display(String lower) {
		return NAMES.getOrDefault(lower, lower);
	}
}
