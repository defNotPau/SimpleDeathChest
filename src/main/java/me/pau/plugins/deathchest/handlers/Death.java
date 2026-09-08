package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import static me.pau.plugins.deathchest.DeathChest.instance;

import java.util.Iterator;
import java.util.List;

public class Death implements Listener {
    Chests deathChests;

    public Death(DeathChest plugin, Chests deathChests) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.deathChests = deathChests;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location chestLocation;

        List<ItemStack> playerDrops = event.getDrops();
        if (playerDrops.isEmpty()) {
            return;
        }

        int chestInventorySize = Math.ceilDiv(playerDrops.size(), 9) * 9;

        ChestMeta customInventory = new ChestMeta(chestInventorySize, player.getName());

        chestLocation = chestPlacement(player.getLocation().getBlock());

        assert chestLocation != null;
        Block block = chestLocation.getBlock();
        if (block.getType() == Material.CHEST) {
            block = block.getRelative(BlockFace.UP);
        }

        block.setType(Material.CHEST);
        block.getState().update(true);

        if (instance.isExcellentEnchantsEnabled()) {
            Enchantment soulbound = Enchantment.getByKey(NamespacedKey.fromString("minecraft:soulbound"));

            Iterator<ItemStack> iterator = playerDrops.iterator();
            while (iterator.hasNext()) {
                ItemStack item = iterator.next();

                if (!item.getEnchantments().containsKey(soulbound)) {
                    customInventory.addItem(item);
                    iterator.remove();
                }
            }

        } else {
            for (ItemStack item : playerDrops) {
                customInventory.addItem(item);
            }
            playerDrops.clear();
        }

        deathChests.put(block, customInventory);
        deathChests.save();
    }

    public static Location chestPlacement(Block blk) {
        World chestW = blk.getWorld();
        double chestX = blk.getX();
        double chestY = blk.getY();
        double chestZ = blk.getZ();

        if (chestW.getEnvironment() == World.Environment.NORMAL) {
            Location worldSpawn = chestW.getSpawnLocation();
            double worldSpawnX = worldSpawn.getX();
            double worldSpawnZ = worldSpawn.getZ();
            double spawnProtection = Bukkit.getSpawnRadius();

            double zDistance = Math.abs(chestZ - worldSpawnZ);
            double xDistance = Math.abs(chestX - worldSpawnX);

            if (xDistance <= spawnProtection && zDistance <= spawnProtection) {
                if (zDistance > xDistance) {
                    if (chestZ - worldSpawnZ > 0) chestZ = worldSpawnZ + spawnProtection + 1;
                    if (chestZ - worldSpawnZ < 0) chestZ = worldSpawnZ - spawnProtection - 1;
                }
                if (xDistance > zDistance) {
                    if (chestX - worldSpawnX > 0) chestX = worldSpawnX + spawnProtection + 1;
                    if (chestX - worldSpawnX < 0) chestX = worldSpawnX - spawnProtection - 1;
                }
            }
        }

        if (chestY >= chestW.getMaxHeight()) chestY = blk.getWorld().getMaxHeight() - 1;
        if (chestY <= chestW.getMinHeight()) chestY = blk.getWorld().getMinHeight() + 1;

        Location mainLoc = new Location(blk.getWorld(), chestX, chestY, chestZ);
        Block main = mainLoc.getBlock();

        if (main.getType() == Material.AIR) {
            return main.getLocation();
        } else {
            for (double i = main.getY(); i <= main.getWorld().getMaxHeight(); i++) {
                Location loc = new Location(main.getWorld(), main.getX(), i, main.getZ());
                Block block = loc.getBlock();

                if (block.getType() == Material.AIR) return new Location(chestW, chestX, i, chestZ);
            }
        }

        return null;
    }
}
