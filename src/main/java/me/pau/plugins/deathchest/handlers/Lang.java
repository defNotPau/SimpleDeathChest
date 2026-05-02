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

    public boolean AgoParameter() {
        if (mainLang.get("time.ago-after") != null) {
            return (boolean) Objects.requireNonNull(mainLang.get("time.ago-after"));
        } else {
            return (boolean) Objects.requireNonNullElse(fallbackLang.get("time.ago-after"), true);
        }
    }

    private FileConfiguration loadLang(String langCode) {
        ensureLangExists(langCode);
        File file = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String key) {
        if (mainLang.get(key) != null) {
            return mainLang.get(key).toString();
        }

        warnPrint("[Critical] Translation not found");
        warnPrint("[Correction] it could be fixed by going to plugins/DeathChest/lang and delete all languages there, then restart the server");
        warnPrint("Warning, /deathchest list command won't work exactly as intended");

        return fallbackLang.getString(key, "[Missing translation]");
    }
}
