package me.pau.plugins.deathchest;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static me.pau.plugins.deathchest.DeathChest.severePrint;

public class Config {
    private final static Config instance = new Config();

    private File file;
    private YamlConfiguration config;

    private boolean coordsCommand;

    private Config() {

    }

    public void load() {
        file = new File(DeathChest.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) DeathChest.getInstance().saveResource("config.yml", false);

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            severePrint(e.toString());
            e.printStackTrace();
        }

        coordsCommand = config.getBoolean("commands.coords");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            severePrint(e.toString());
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);

        save();
    }

    public boolean getCoordsState() {
        return coordsCommand;
    }

    public void setCoordsState(boolean value) {
        this.coordsCommand = value;

        set("commands.coords", value);
    }

    public static Config getInstance() {
        return instance;
    }
}
