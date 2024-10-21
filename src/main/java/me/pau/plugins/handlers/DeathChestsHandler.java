package me.pau.plugins.handlers;

import me.pau.plugins.DeathChest;
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
import java.util.Set;

public class DeathChestsHandler {
    private final JavaPlugin plugin;
    private final HashMap<Block, Inventory> deathChests = new HashMap<>();
    
    Utils utils;

    public DeathChestsHandler(DeathChest plugin) {
        this.plugin = plugin;
        utils = new Utils(plugin);
    }

    public void put(Block block, Inventory inventory) { deathChests.put(block, inventory); }

    public Inventory get(Block key) {
        return deathChests.get(key);
    }

    public Block get(Inventory value) {
        for (Block i : deathChests.keySet()) {
            if (deathChests.get(i) == value) {
                return i;
            }
        }
        return null;
    }

    public boolean containsKey(Block key) {
        return deathChests.containsKey(key);
    }

    public boolean containsValue(Inventory value) {
        return deathChests.containsValue(value);
    }

    public void remove(Block key) { deathChests.remove(key); }

    public Set<Block> keySet() {
        return deathChests.keySet();
    }

    private void deleteFile(File file) {
        FileConfiguration emptyConfig = new YamlConfiguration();
        try {
            emptyConfig.save(file);
        } catch (IOException e) {
            utils.warnPrint(e.toString());
        }
    }

    public void save() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        deleteFile(file);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Block block : deathChests.keySet()) {
            String locString = block.getLocation().getWorld().getName() + "," +
                    block.getX() + "," + block.getY() + "," + block.getZ();
            Inventory inventory = deathChests.get(block);

            config.set(locString, inventory.getContents());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            utils.warnPrint(e.toString());
        }
    }

    public void load() {
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

        deleteFile(file);
    }
}

