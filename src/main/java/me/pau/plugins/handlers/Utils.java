package me.pau.plugins.handlers;

import me.pau.plugins.DeathChest;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils {
    private final JavaPlugin plugin;

    public Utils(DeathChest plugin) {
        this.plugin = plugin;
    }

    public void infoPrint(String msg) {
        plugin.getLogger().info(msg);
    }

    public void warnPrint(String msg) {
        plugin.getLogger().warning(msg);
    }
}
