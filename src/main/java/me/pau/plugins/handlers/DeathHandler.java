package me.pau.plugins.handlers;
import me.pau.plugins.deathchest;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class DeathHandler implements Listener {
    public HashMap<Chest, String> deathChests = new HashMap<>();

    public DeathHandler(deathchest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        Location deathLocation = player.getLocation();

        List<ItemStack> playerDrops = event.getDrops();

        if (player.getWorld().getEnvironment() == World.Environment.THE_END && player.getY() <= 0) {
            Location chestLocation = new Location(player.getWorld(), player.getX(), 1, player.getZ());
            Block block = chestLocation.getBlock();

            block.setType(Material.CHEST);
            block.getState().update(true);

            Chest chest = (Chest) block.getState();
            Inventory chestInventory = chest.getBlockInventory();

            for (ItemStack item : playerDrops) {
                chestInventory.addItem(item);
            }

            playerDrops.clear();
            deathChests.put(chest, player.getName());
        } else {
            Block block = deathLocation.getBlock();
            block.setType(Material.CHEST);

            block.getState().update(true);

            Chest chest = (Chest) block.getState();
            Inventory chestInventory = chest.getBlockInventory();

            for (ItemStack item : playerDrops) {
                chestInventory.addItem(item);
            }

            playerDrops.clear();
            deathChests.put(chest, player.getName());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();

        if (brokenBlock.getType() == Material.CHEST) {
            Chest brokenChest = (Chest) brokenBlock.getState();
            if (deathChests.containsKey(brokenChest)) {
                if (brokenChest.getBlockInventory().isEmpty()) {
                    deathChests.remove(brokenChest);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}

// deathChests.remove(brokenBlock);
// event.setCancelled(true);