/*

package me.pau.plugins.handlers;
import me.pau.plugins.deathchest;
import me.pau.plugins.handlers.SaveHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RestoreHandler {
    private final JavaPlugin plugin;
    public HashMap<Block, Inventory> deathChests = new HashMap<>();

    SaveHandler saveHandler;

    public RestoreHandler(deathchest plugin) {
        this.plugin = plugin;

        saveHandler = new SaveHandler(plugin);
        saveHandler.loadDeathChests();
    }

    public void restore() {
        for (Block chest : deathChests.keySet()) {

        }
    }
}

*/