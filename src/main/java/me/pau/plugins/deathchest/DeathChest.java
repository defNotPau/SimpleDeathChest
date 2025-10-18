package me.pau.plugins.deathchest;

import me.pau.plugins.deathchest.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        // Config.yml stuff
        instance.saveDefaultConfig();
        playerBreakable = this.getConfig().getBoolean("chest_interactions.player_breakable", false);
        explosionProof = this.getConfig().getBoolean("chest_interactions.explosion_proof", true);
        dropItemsWhenExploded = this.getConfig().getBoolean("chest_interactions.items_drop_when_exploded", true);
        dropItemsWhenBroken = this.getConfig().getBoolean("chest_interactions.items_drop_when_broken", true);

        nameVisible = this.getConfig().getBoolean("chest_customization.name_on_chest", true);

        // Anything to do with integrations
        isExcellentEnchantsEnabled = getServer().getPluginManager().isPluginEnabled("ExcellentEnchants");

        // Chests class summoning
        chests = new Chests(instance);
        chests.load();

        // If there are any chests loaded onEnable, log the count
        if (!chests.getAllBlocks().isEmpty()) {
            infoPrint("There are " + chests.getAllBlocks().size() + " death chests loaded in the world.");
        }

        death = new Death(instance, chests);
        interaction = new Interaction(instance, chests);
        chests.restoreInWorld();

        // Register command
        Objects.requireNonNull(getCommand("deathchest")).setExecutor(this);
    }

    // Called by Death handler when a chest is created

    // Command handler
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[SimpleDeathChest] Only players can use this command.");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all death chests for this player, and also show unknown owner/time
            int i = 1;
            player.sendMessage("[SimpleDeathChest] Your death chests are at:");
            Instant now = Instant.now();
            for (Map.Entry<Block, ChestMeta> entry : chests.entrySet()) {
                Block block = entry.getKey();
                ChestMeta meta = entry.getValue();
                if (meta.getOwner() == null || meta.getCreated() == null) {
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (unknown owner/time)",
                        i++, block.getX(), block.getY(), block.getZ()));
                } else if (meta.getOwner().equals(player.getUniqueId())) {
                    Duration duration = Duration.between(meta.getCreated(), now);
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
     * IMPORTANT,
     * STOPS THE PLUGIN
     * COMPLETELY
     *
     * @param msg message to be logged as ERROR on the server's console as the plugin shuts down
     */
    static public void error(String msg) {
        instance.getLogger().severe(msg);
        instance.getLogger().warning("Plugin is shutting down due to a critical error");
        instance.getLogger().info("(Ignore error below that says \"The plugin classloader for DeathChest has thrown a zip file error\")");
        Bukkit.getPluginManager().disablePlugin(instance);
    }

    /**
     * @return what does the name of this function say?
     */
    public boolean isExcellentEnchantsEnabled() {
        return isExcellentEnchantsEnabled;
    }
}
