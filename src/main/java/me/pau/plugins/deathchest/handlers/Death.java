package me.pau.plugins.deathchest.handlers;
import me.pau.plugins.deathchest.DeathChest;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Death implements Listener {

    Chests deathChests;

    public Death(DeathChest plugin, Chests deathChests) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.deathChests = deathChests;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location chestLocation;

        List<ItemStack> playerDrops = event.getDrops();
        if (playerDrops.isEmpty()) { return; }

        Inventory customInventory = Bukkit.createInventory(null, 45);

        if (player.getY() <= player.getWorld().getMaxHeight()) {
            chestLocation = new Location(player.getWorld(), player.getX(), 1, player.getZ());
        } else {
            chestLocation = event.getPlayer().getLocation();
        }

        Block block = chestLocation.getBlock();
        if (block.getType() == Material.CHEST) { block = block.getRelative(BlockFace.UP); }

        block.setType(Material.CHEST);
        block.getState().update(true);

        for (ItemStack item : playerDrops) {
            customInventory.addItem(item);
        }

        playerDrops.clear();

        deathChests.put(block, customInventory, player);
        deathChests.save();
    }
}



