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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class DeathHandler implements Listener {
    public HashMap<Block, Inventory> deathChests = new HashMap<>();

    public DeathHandler(deathchest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
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