package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.Objects;

import static me.pau.plugins.deathchest.DeathChest.*;

public class Lang {
    private final FileConfiguration mainLang;
    private final FileConfiguration fallbackLang;
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
        this.fallbackLang = loadLang("en");
    }

    private FileConfiguration loadLang(String langCode) {
        ensureLangExists(langCode);
        File file = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String key) {
        if (mainLang.get(key) != null) {
            return Objects.requireNonNull(mainLang.get(key)).toString();
        } else {
            severePrint("[Critical] Translation not found, but do not worry ;)");
            severePrint("[Correction] go to plugins/DeathChest/lang and delete all languages there, then restart the server");
            warnPrint("Warning, /deathchest list command won't work exactly as intended");
            return Objects.requireNonNull(fallbackLang.get(key)).toString();
        }
    }
}
