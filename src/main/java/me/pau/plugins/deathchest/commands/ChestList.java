package me.pau.plugins.deathchest.commands;

import me.pau.plugins.deathchest.handlers.ChestMeta;
import me.pau.plugins.deathchest.handlers.Chests;
import me.pau.plugins.deathchest.handlers.Lang;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

// Original implementation by nickmartin1ee7 on github
public class ChestList implements CommandExecutor {
    private final Chests chests;
    private final Lang lang;

    public ChestList(Chests chests, Lang lang) {
        this.chests = chests;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(String.format("[SimpleDeathChest] %s", lang.translate("list.start", null)));
            return true;
        }
        player.locale();
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all death chests for this player, and also show unknown owner/time
            int i = 1;
            player.sendMessage(String.format("[SimpleDeathChest] %s", lang.translate("list.start", player)));
            Instant now = Instant.now();
            for (Map.Entry<Block, ChestMeta> entry : chests.entrySet()) {
                Block block = entry.getKey();
                ChestMeta meta = entry.getValue();
                if (Objects.equals(meta.getOwnerName(), "unknown") || meta.getCreated() == null) {
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (%s)",
                            i++, block.getX(), block.getY(), block.getZ(), lang.translate("list.unknown", player)));
                } else if (meta.getOwner().equals(player.getUniqueId())) {
                    Duration duration = Duration.between(meta.getCreated(), now);
                    String timeAgo = formatDuration(duration);
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (%s ago) (%s)",
                            i++, block.getX(), block.getY(), block.getZ(), timeAgo, lang.translate("list.yours", player)));
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
}
