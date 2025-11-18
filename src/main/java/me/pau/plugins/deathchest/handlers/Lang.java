package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Objects;

public class Lang {
    private final Map<String, FileConfiguration> langFiles = new HashMap<>();
    private final DeathChest plugin;

    private void ensureLangExists(String langCode) {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        File langFile = new File(langFolder, langCode + ".yml");

        if (!langFile.exists()) {
            plugin.saveResource("lang/" + langCode + ".yml", false);
        }
    }

    public Lang(DeathChest plugin) {
        this.plugin = plugin;
        ensureLangExists("en");

        String[] languages = {"en", "es"};
        for (String i : languages) {
            langFiles.put("i", loadLang(i));
        }
    }

    private FileConfiguration getLangFile(String langCode) {
        return (langFiles.get(langCode) == null) ? langFiles.get("en") : (langFiles.get(langCode));
    }

    private FileConfiguration loadLang(String langCode) {
        ensureLangExists(langCode);
        File file = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String key, Player player) {
        String translation;

        String langCode = (player != null) ? player.locale().toString().toLowerCase(Locale.ROOT) : "en_us";
        FileConfiguration lang = getLangFile(langCode);

        translation = Objects.requireNonNull(lang.get(key)).toString();

        return translation;
    }
}
