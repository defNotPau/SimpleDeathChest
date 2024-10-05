package me.pau.plugins.handlers;

import me.pau.plugins.deathchest;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SaveHandler {
    private final JavaPlugin plugin;
    private HashMap<Block, Inventory> deathChests = new HashMap<>();

    public SaveHandler(deathchest plugin) {
        this.plugin = plugin;
    }

    public void put(Block block, Inventory inventory) {
        plugin.getLogger().info("put() Executed");
        plugin.getLogger().info(inventory.toString());
        deathChests.put(block, inventory);
    }

    public Inventory get(Block key) {
        plugin.getLogger().info("get() Executed");
        return deathChests.get(key);
    }

    public boolean containsKey(Block key) {
        plugin.getLogger().info("containsKey() Executed");
        return deathChests.containsKey(key);
    }

    public void remove(Block key) {
        plugin.getLogger().info("remove() Executed");
        deathChests.remove(key);
    }

    public void saveDeathChests() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Block block : deathChests.keySet()) {
            String locString = block.getLocation().getWorld().getName() + "," +
                    block.getX() + "," + block.getY() + "," + block.getZ();
            Inventory inventory = deathChests.get(block);

            config.set(locString, inventory.getContents());
        }

        try {
            config.save(file);
            plugin.getLogger().info("Saving Death Chests");
        } catch (IOException e) {
            plugin.getLogger().info(e.getLocalizedMessage());
        }
    }

    public void loadDeathChests() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");

        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String locString : config.getKeys(false)) {
            String[] locParts = locString.split(",");
            World world = Bukkit.getWorld(locParts[0]);
            int x = Integer.parseInt(locParts[1]);
            int y = Integer.parseInt(locParts[2]);
            int z = Integer.parseInt(locParts[3]);

            Location location = new Location(world, x, y, z);
            Block block = location.getBlock();

            List<ItemStack> contents = (List<ItemStack>) config.get(locString);
            Inventory customInventory = Bukkit.createInventory(null, 45);

            assert contents != null;
            customInventory.setContents(contents.toArray(new ItemStack[0]));

            deathChests.put(block, customInventory);
        }

        plugin.getLogger().info("Loading Death Chests");
    }
}

