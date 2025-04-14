package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.pau.plugins.deathchest.DeathChest.infoPrint;
import static me.pau.plugins.deathchest.DeathChest.warnPrint;

public class Chests {
    private final JavaPlugin plugin;

    private final HashMap<Block, Inventory> deathChests = new HashMap<>();
    private final HashMap<Inventory, Block> opposite = new HashMap<>();

    public Chests(DeathChest plugin) {
        this.plugin = plugin;
    }

    /**
     * @param block the chest-block as a key for the inventory
     * @param inventory the inventory where the player's items are in assigned to the chest
     */
    public void put(Block block, Inventory inventory) {
        deathChests.put(block, inventory);
        opposite.put(inventory, block);
    }

    /**
     * @param key block for where the inventory should be in
     * @return value of the block (key) on the hash map of block, inventory where deathchest information is stored
     */
    public Inventory get(Block key) {
        return deathChests.get(key);
    }

    /**
     * @param value inventory for which the block should be the owner of
     * @return value of the block on the hash map based on the inventory it owns
     */
    public Block get(Inventory value) {
        return opposite.get(value);
    }

    /**
     * @param key block that will be checked if part of the hash map
     * @return whether the block is part of the hash map
     */
    public boolean containsKey(Block key) {
        return deathChests.containsKey(key);
    }

    /**
     * @param value inventory value that will be checked if it is part of the hash map
     * @return whether the inventory is a value of the hash map
     */
    public boolean containsValue(Inventory value) {
        return deathChests.containsValue(value);
    }

    /**
     * @param key block-key of the hash map to be removed
     */
    public void remove(Block key) {
        deathChests.remove(key);
        opposite.remove(deathChests.get(key));
    }

    /**
     * @param file file to be deleted, as rewriting it as an empty YamlConfiguration
     */
    private void deleteFile(File file) {
        FileConfiguration emptyConfig = new YamlConfiguration();
        try {
            emptyConfig.save(file);
        } catch (IOException e) {
            warnPrint(e.toString());
        }
    }

    /**
     * Saves current state of the hash map containing chests and inventories to a YamlConfiguration
     */
    public void save() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        FileConfiguration emptyConfig = new YamlConfiguration();
        
        try {
            emptyConfig.save(file);
        } catch (IOException e) {
            warnPrint(e.toString());
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Block block : deathChests.keySet()) {
            String locString = block.getLocation().getWorld().getName() + "," +
                    block.getX() + "," + block.getY() + "," + block.getZ();
            Inventory inventory = deathChests.get(block);

            config.set(locString, inventory.getContents());
        }

        try {
            config.save(file);
            infoPrint("Saving Death Chests");
        } catch (IOException e) {
            warnPrint(e.toString());
        }
    }

    /**
     * Loads the new state of the hash map containing chests and inventories from the saved YamlConfiguration
     */
    public void load() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");

        if (file.exists()) {
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
        }

        deleteFile(file);
        infoPrint("Loaded Death Chests");
    }

    /**
     * Chests in the world are added to the world as in the hash map
     */
    public void restoreInWorld() {
        for (Block chest : deathChests.keySet()) {
            if (chest != null && chest.getLocation().getChunk().isLoaded()) {
                chest.setType(Material.CHEST);
            } else {
                assert chest != null;
                warnPrint("Skipped restoring chest at unloaded chunk: " + chest.getLocation());
            }
        }
    }
}
