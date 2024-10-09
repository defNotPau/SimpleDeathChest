package me.pau.plugins.handlers;
import me.pau.plugins.deathchest;

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

public class DeathHandler implements Listener {

    DeathChestsHandler deathChests;

    public DeathHandler(deathchest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        deathChests = new DeathChestsHandler(plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location chestLocation;

        if (player.getY() <= 0) {
            chestLocation = new Location(player.getWorld(), player.getX(), 1, player.getZ());
        } else {
            chestLocation = event.getPlayer().getLocation();
        }

        List<ItemStack> playerDrops = event.getDrops();
        Inventory customInventory = Bukkit.createInventory(null, 45);

        Block block = chestLocation.getBlock();

        block.setType(Material.CHEST);
        block.getState().update(true);

        for (ItemStack item : playerDrops) {
            customInventory.addItem(item);
        }

        playerDrops.clear();

        deathChests.put(block, customInventory);
        deathChests.save();
    }
}



