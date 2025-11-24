package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.Objects;

import static me.pau.plugins.deathchest.DeathChest.language;

public class Lang {
    private final FileConfiguration mainLang;
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
        this.mainLang = loadLang(language);
    }

    private FileConfiguration loadLang(String langCode) {
        ensureLangExists(langCode);
        File file = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String key) {
        return Objects.requireNonNull(mainLang.get(key)).toString();
    }
}
