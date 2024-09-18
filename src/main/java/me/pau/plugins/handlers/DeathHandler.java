package me.pau.plugins.handlers;
import me.pau.plugins.deathchest;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DeathHandler implements Listener {
    private final JavaPlugin plugin;
    public HashMap<Block, Inventory> deathChests = new HashMap<>();

    public DeathHandler(deathchest plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        } catch (IOException e) {
            e.printStackTrace();
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
            customInventory.setContents(contents.toArray(new ItemStack[0]));

            deathChests.put(block, customInventory);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location chestLocation;

        if (player.getWorld().getEnvironment() == World.Environment.THE_END && player.getY() <= 0) {
            chestLocation = new Location(player.getWorld(), player.getX(), 1, player.getZ());
        } else {
            chestLocation = event.getPlayer().getLocation();
        }

        List<ItemStack> playerDrops = event.getDrops();
        Inventory customInventory = Bukkit.createInventory(null, 45, "§5Death §5chest");

        Block block = chestLocation.getBlock();

        block.setType(Material.CHEST);
        block.getState().update(true);

        for (ItemStack item : playerDrops) {
            customInventory.addItem(item);
        }

        playerDrops.clear();
        deathChests.put(block, customInventory);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();

        if (brokenBlock.getType() == Material.CHEST) {
            if (deathChests.containsKey(brokenBlock)) {
                if (deathChests.get(brokenBlock).isEmpty()) {
                    event.setDropItems(false);
                    deathChests.remove(brokenBlock);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.CHEST) {
                Block chest = clickedBlock.getLocation().getBlock();

                if (deathChests.containsKey(chest)) {
                    Player player = event.getPlayer();
                    player.openInventory(deathChests.get(chest));
                    event.setCancelled(true);
                }
            }
        }
    }
}