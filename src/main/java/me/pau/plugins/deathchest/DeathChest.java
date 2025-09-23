package me.pau.plugins.deathchest;

import me.pau.plugins.deathchest.handlers.*;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathChest extends JavaPlugin {
    public static DeathChest instance;

    // Integrations' variables
    private boolean isExcellentEnchantsEnabled = false;

    // Config variables
    static public boolean playerBreakable;
    static public boolean dropItemsWhenBroken;
    static public boolean explosionProof;
    static public boolean dropItemsWhenExploded;

    static public boolean nameVisible;

    // Other classes that should be summoned
    Death death;
    Interaction interaction;
    Chests chests;

    // Track chest creation times and owners
    private final Map<Block, ChestMeta> chestMetaMap = new HashMap<>();

    public static class ChestMeta {
        public final UUID owner;
        public final Instant created;

        public ChestMeta(UUID owner, Instant created) {
            this.owner = owner;
            this.created = created;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        // Config.yml stuffs
        instance.saveDefaultConfig();
        playerBreakable = this.getConfig().getBoolean("chest_interactions.player_breakable", false);
        explosionProof = this.getConfig().getBoolean("chest_interactions.explosion_proof", true);
        dropItemsWhenExploded = this.getConfig().getBoolean("chest_interactions.items_drop_when_exploded", true);
        dropItemsWhenBroken = this.getConfig().getBoolean("chest_interactions.items_drop_when_broken", true);

        nameVisible = this.getConfig().getBoolean("chest_customization.name_on_chest", true);

        // Anything to do with integrations such as variables checking if a plugin is
        // enabled
        isExcellentEnchantsEnabled = getServer().getPluginManager().isPluginEnabled("ExcellentEnchants");

        // Main class summoning
        chests = new Chests(instance);
        chests.load();

        // Register all loaded chests as tracked (owner/time unknown)
        for (Block block : chests.getAllBlocks()) {
            if (!chestMetaMap.containsKey(block)) {
                chestMetaMap.put(block, new ChestMeta(null, null));
            }
        }

        // If there are any chests loaded onEnable, log the count
        if (!chests.getAllBlocks().isEmpty()) {
            infoPrint("There are " + chests.getAllBlocks().size() + " death chests loaded in the world.");
        }

        death = new Death(instance, chests);
        interaction = new Interaction(instance, chests);
        chests.restoreInWorld();

        // Register command
        getCommand("deathchest").setExecutor(this);
    }

    // Called by Death handler when a chest is created
    public void registerDeathChest(Block block, UUID owner) {
        chestMetaMap.put(block, new ChestMeta(owner, Instant.now()));
    }

    // Called by Death handler when a chest is removed
    public void unregisterDeathChest(Block block) {
        chestMetaMap.remove(block);
    }

    // Command handler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[SimpleDeathChest] Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all death chests for this player, and also show unknown owner/time
            int i = 1;
            player.sendMessage("[SimpleDeathChest] Your death chests are at:");
            Instant now = Instant.now();
            for (Map.Entry<Block, ChestMeta> entry : chestMetaMap.entrySet()) {
                Block block = entry.getKey();
                ChestMeta meta = entry.getValue();
                if (meta.owner == null || meta.created == null) {
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (unknown owner/time)",
                        i++, block.getX(), block.getY(), block.getZ()));
                } else if (meta.owner.equals(player.getUniqueId())) {
                    Duration duration = Duration.between(meta.created, now);
                    String timeAgo = formatDuration(duration);
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (%s ago)",
                        i++, block.getX(), block.getY(), block.getZ(), timeAgo));
                }
            }
            if (i == 1) {
                player.sendMessage("[SimpleDeathChest] You have no active death chests.");
            }
            return true;
        }
        player.sendMessage("[SimpleDeathChest] Usage: /deathchest list");
        return true;
    }

    // Helper to format duration as '4 minutes', '1 day and 25 minutes', etc.
    private static String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        if (days > 0) {
            return String.format("%d day%s and %d minute%s",
                    days, days == 1 ? "" : "s",
                    minutes, minutes == 1 ? "" : "s");
        } else if (hours > 0) {
            return String.format("%d hour%s and %d minute%s",
                    hours, hours == 1 ? "" : "s",
                    minutes, minutes == 1 ? "" : "s");
        } else {
            return String.format("%d minute%s",
                    minutes, minutes == 1 ? "" : "s");
        }
    }

    @Override
    public void onDisable() {
        chests.save();
        warnPrint("I'm def NOT working rn");
    }

    /**
     * @param msg message to be logged as INFO on the server's console
     */
    static public void infoPrint(String msg) {
        instance.getLogger().info(msg);
    }

    /**
     * @param msg message to be logged as WARN on the server's console
     */
    static public void warnPrint(String msg) {
        instance.getLogger().warning(msg);
    }

    /**
     * @return what does the name of this function say?
     */
    public boolean isExcellentEnchantsEnabled() {
        return isExcellentEnchantsEnabled;
    }
}
