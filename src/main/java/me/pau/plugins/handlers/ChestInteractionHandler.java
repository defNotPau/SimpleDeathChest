package me.pau.plugins.handlers;
import me.pau.plugins.deathchest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestInteractionHandler implements Listener {
    DeathChestsHandler deathChests;

    public ChestInteractionHandler(deathchest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        deathChests = new DeathChestsHandler(plugin);
        deathChests.load();
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

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        deathChests.save();
        if (deathChests.containsValue(event.getInventory())) {
            if (event.getInventory().isEmpty()) {
                Block block = deathChests.get(event.getInventory());
                deathChests.remove(block);

                block.setType(Material.AIR);
                deathChests.save();
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block ->
                block.getType() == Material.CHEST && deathChests.containsKey(block)
        );
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block ->
                block.getType() == Material.CHEST && deathChests.containsKey(block)
        );
    }
}

