package me.pau.plugins.handlers;

import me.pau.plugins.DeathChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class DeathChestsHandler {
    private final JavaPlugin plugin;
    private HashMap<Block, Inventory> deathChests = new HashMap<>();
    private HashMap<Location, Player> lastestDeathLocation = new HashMap<>();

    Utils utils;

    public DeathChestsHandler(DeathChest plugin) {
        this.plugin = plugin;
        utils = new Utils(plugin);
    }

    public void put(Block block, Inventory inventory, Player player) {
        utils.infoPrint("put() Executed");
        utils.infoPrint(inventory.toString());
        deathChests.put(block, inventory);


        if (lastestDeathLocation.containsValue(player)) for (Location i : lastestDeathLocation.keySet()) {
            if (lastestDeathLocation.get(i) == player) {
                lastestDeathLocation.remove(i);
            }
        }
        lastestDeathLocation.put(block.getLocation(), player);
    }

    public Inventory get(Block key) {
        utils.infoPrint("get(block) Executed");
        return deathChests.get(key);
    }

    public Block get(Inventory value) {
        utils.infoPrint("get(inventory) Executed");
        for (Block i : deathChests.keySet()) {
            if (deathChests.get(i) == value) {
                return i;
            }
        }
        return null;
    }

    public Location get(Player value) {
        utils.infoPrint("get(inventory) Executed");
        for (Location i : lastestDeathLocation.keySet()) {
            if (lastestDeathLocation.get(i) == value) {
                return i;
            }
        }
        return null;
    }

    public boolean containsKey(Block key) {
        utils.infoPrint("containsKey() Executed");
        return deathChests.containsKey(key);
    }

    public boolean containsValue(Inventory value) {
        utils.infoPrint("containsValue() Executed");
        return deathChests.containsValue(value);
    }

    public void remove(Block key) {
        utils.infoPrint("remove() Executed");
        deathChests.remove(key);
        lastestDeathLocation.remove(key.getLocation());
        utils.infoPrint(Integer.toString(deathChests.size()));
    }

    public Set<Block> keySet() {
        utils.infoPrint("keySet() Executed");
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
        utils.infoPrint(Integer.toString(deathChests.size()));

        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        File latest = new File(plugin.getDataFolder(), "latestDeaths.yml");

        FileConfiguration emptyConfig = new YamlConfiguration();
        FileConfiguration emptyConfig2 = new YamlConfiguration();
        try {
            emptyConfig.save(file);
            emptyConfig2.save(latest);
        } catch (IOException e) {
            utils.warnPrint(e.toString());
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration latestConfig = YamlConfiguration.loadConfiguration(latest);

        for (Block block : deathChests.keySet()) {
            String locString = block.getLocation().getWorld().getName() + "," +
                    block.getX() + "," + block.getY() + "," + block.getZ();
            Inventory inventory = deathChests.get(block);

            config.set(locString, inventory.getContents());
        }

        for (Location location : lastestDeathLocation.keySet()) {
            String locString = location.getX() + "," + location.getY() + "," + location.getZ();
            Player plr = lastestDeathLocation.get(location);

            latestConfig.set(locString, plr.getName());
        }

        try {
            config.save(file);
            latestConfig.save(latest);
            utils.infoPrint("Saving Death Chests");
        } catch (IOException e) {
            utils.warnPrint(e.toString());
        }
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "deathChests.yml");
        File latest = new File(plugin.getDataFolder(), "latestDeaths.yml");
        if (!file.exists() || !latest.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration latestConfig = YamlConfiguration.loadConfiguration(latest);

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

        for (String locString : latestConfig.getKeys(false)) {
            String[] locParts = locString.split(",");
            int x = Integer.parseInt(locParts[0]);
            int y = Integer.parseInt(locParts[1]);
            int z = Integer.parseInt(locParts[2]);
            Location location = new Location(null, x, y, z);

            Player player = Bukkit.getPlayer(locParts[3]);

            assert player != null;
            lastestDeathLocation.put(location, player);
        }


        deleteFile(file);
        deleteFile(latest);
        utils.infoPrint("Loaded Death Chests");
    }
}

