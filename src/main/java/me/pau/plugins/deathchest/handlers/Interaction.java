package me.pau.plugins.deathchest.handlers;
import me.pau.plugins.deathchest.DeathChest;

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

import static me.pau.plugins.deathchest.DeathChest.playerBreakable;
import static me.pau.plugins.deathchest.DeathChest.explosionProof;

public class Interaction implements Listener {
    Chests deathChests;

    public Interaction(DeathChest plugin, Chests deathChests) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.deathChests = deathChests;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (playerBreakable) { return; }

        Block brokenBlock = event.getBlock();
        if (!deathChests.containsKey(brokenBlock)) { return; }
        if (!deathChests.get(brokenBlock).isEmpty()) { event.setCancelled(true);}
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) { return; }

        if (deathChests.containsKey(clickedBlock)) {
            Player player = event.getPlayer();
            player.openInventory(deathChests.get(clickedBlock));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if (!deathChests.containsValue(event.getInventory())) { return; }
        if (!event.getInventory().isEmpty()) { return; }

        Block block = deathChests.get(event.getInventory());
        deathChests.remove(block);

        block.setType(Material.AIR);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!explosionProof) { return; }
        event.blockList().removeIf(block ->
                block.getType() == Material.CHEST && deathChests.containsKey(block)
        );
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!explosionProof) { return; }
        event.blockList().removeIf(block ->
                block.getType() == Material.CHEST && deathChests.containsKey(block)
        );
    }
}

