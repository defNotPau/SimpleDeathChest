package me.pau.plugins.deathchest.handlers;

import me.pau.plugins.deathchest.DeathChest;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.NamespacedKey;
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
        double chestY = blk.getY();
        if (blk.getLocation().getY() <= blk.getWorld().getMinHeight()) { chestY = blk.getWorld().getMinHeight() + 1; }
        if (blk.getLocation().getY() >= blk.getWorld().getMaxHeight()) { chestY = blk.getWorld().getMaxHeight() - 1; }

        Location mainLoc = new Location(blk.getWorld(), blk.getX(), chestY, blk.getZ());
        Block main = mainLoc.getBlock();

        if (main.getType() == Material.AIR) {
            return main.getLocation();
        } else {
            for (double i = main.getY(); i <= main.getWorld().getMaxHeight(); i++) {
                Location loc = new Location(main.getWorld(), main.getX(), i, main.getZ());
                Block block = loc.getBlock();

                if (block.getType() == Material.AIR) return loc;
            }
        }

        return null;
    }
}
