package me.pau.plugins.handlers;

import me.pau.plugins.DeathChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.pau.plugins.DeathChest.infoPrint;
import static me.pau.plugins.DeathChest.warnPrint;

public class Chests {
    private final JavaPlugin plugin;
    private final HashMap<Block, Inventory> deathChests = new HashMap<>();
    private final HashMap<String, Location> latestDeathLocation = new HashMap<>();

    public Chests(DeathChest plugin) {
        this.plugin = plugin;
    }

    public void put(Block block, Inventory inventory, Player player) {
        // infoPrint("put() Executed");
        // infoPrint(inventory.toString());
        deathChests.put(block, inventory);


        if (latestDeathLocation.containsValue(player)) for (String i : latestDeathLocation.keySet()) {
            if (latestDeathLocation.get(i) == block.getLocation()) {
                latestDeathLocation.remove(i);
            }
        }
        latestDeathLocation.put(player.getName(), block.getLocation());
    }

    public Inventory get(Block key) {
        // infoPrint("get(block) Executed");
        return deathChests.get(key);
    }

    public Block get(Inventory value) {
        // infoPrint("get(inventory) Executed");
        for (Block i : deathChests.keySet()) {
            if (deathChests.get(i) == value) {
                return i;
            }
        }
        return null;
    }

    public Location get(String value) {
        // infoPrint("get(player) Executed");
        return latestDeathLocation.get(value);
    }

    public String get(Location value) {
        // infoPrint("get(location) Executed");
        for (String i : latestDeathLocation.keySet()) {
            if (latestDeathLocation.get(i) == value) {
                return i;
            }
        }
        return null;
    }

    public boolean containsKey(Block key) {
        // infoPrint("containsKey() Executed");
        return deathChests.containsKey(key);
    }

    public boolean containsValue(Inventory value) {
        // infoPrint("containsValue() Executed");
        return deathChests.containsValue(value);
    }

    public void remove(Block key) {
        // infoPrint("remove() Executed");
        deathChests.remove(key);
        latestDeathLocation.remove(get(key.getLocation()));
        // infoPrint(Integer.toString(deathChests.size()));
    }

    public Set<Block> keySet() {
        // infoPrint("keySet() Executed");
        return deathChests.keySet();
    }

    private void deleteFile(File file) {
        FileConfiguration emptyConfig = new YamlConfiguration();
        try {
            emptyConfig.save(file);
        } catch (IOException e) {
            warnPrint(e.toString());
        }
    }

    public void save() {
        // infoPrint(Integer.toString(deathChests.size()));

        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        File latest = new File(plugin.getDataFolder(), "latestDeaths.yml");

        FileConfiguration emptyConfig = new YamlConfiguration(), emptyConfig2 = new YamlConfiguration();
        
        try {
            emptyConfig.save(file);
            emptyConfig2.save(latest);
        } catch (IOException e) {
            warnPrint(e.toString());
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration latestConfig = YamlConfiguration.loadConfiguration(latest);

        for (Block block : deathChests.keySet()) {
            String locString = block.getLocation().getWorld().getName() + "," +
                    block.getX() + "," + block.getY() + "," + block.getZ();
            Inventory inventory = deathChests.get(block);

            config.set(locString, inventory.getContents());
        }

        for (String plr : latestDeathLocation.keySet()) {
            Location loc = latestDeathLocation.get(plr);

            String locKey = loc.getWorld().getName() + "," +
                    loc.getX() + "," + loc.getY() + "," + loc.getZ();

            latestConfig.set(plr, locKey);
        }

        try {
            config.save(file);
            latestConfig.save(latest);
            infoPrint("Saving Death Chests");
        } catch (IOException e) {
            warnPrint(e.toString());
        }
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        File latest = new File(plugin.getDataFolder(), "latestDeaths.yml");

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

                customInventory.setContents(contents.toArray(new ItemStack[0]));

                deathChests.put(block, customInventory);
            }
            // infoPrint("death chests loaded");
        }

        if (latest.exists()) {
            FileConfiguration latestConfig = YamlConfiguration.loadConfiguration(latest);
            for (String key : latestConfig.getKeys(false)) {
                Location loc = deserializeLocation((String) latestConfig.get(key));
                String plrName = key;

                // warnPrint(loc + " " + plrName);
                latestDeathLocation.put(plrName, loc);
            }
            // infoPrint("latest deaths loaded");
        }

        deleteFile(file);
        deleteFile(latest);
        infoPrint("Loaded Death Chests");
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

    public void restoreInWorld() {
        for (Block chest : deathChests.keySet()) {
            if (chest != null && chest.getLocation().getChunk().isLoaded()) {
                chest.setType(Material.CHEST);
            } else {
                assert chest != null;
                warnPrint("Skipped restoring chest at unloaded chunk: " + chest.getLocation());
            }
        }

        // infoPrint("Death Chest restore in world complete");
    }
}
