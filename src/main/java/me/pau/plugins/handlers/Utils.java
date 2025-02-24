package me.pau.plugins.handlers;

import me.pau.plugins.DeathChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
    public void severePrint(String msg) {
        plugin.getLogger().severe(msg);
    }

    public Location deserializeLocation(String locKey) {
        String[] parts = locKey.split(",");
        if (parts.length == 4) {
            World world = Bukkit.getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            return new Location(world,x,y,z);
        }
        return null;
    }
}
