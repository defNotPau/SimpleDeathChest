package me.pau.plugins.deathchest.commands;

import me.pau.plugins.deathchest.handlers.ChestMeta;
import me.pau.plugins.deathchest.handlers.Chests;
import me.pau.plugins.deathchest.handlers.Lang;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(String.format("[SimpleDeathChest] %s", lang.translate("list.nonPlayer")));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all death chests for this player, and also show unknown owner/time
            int i = 1;
            player.sendMessage(String.format("[SimpleDeathChest] %s", lang.translate("list.start")));
            Instant now = Instant.now();
            for (Map.Entry<Block, ChestMeta> entry : chests.entrySet()) {
                Block block = entry.getKey();
                ChestMeta meta = entry.getValue();
                if (Objects.equals(meta.getOwnerName(), "unknown") || meta.getCreated() == null) {
                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (%s)",
                            i++, block.getX(), block.getY(), block.getZ(), lang.translate("list.unknown")));
                } else if (meta.getOwner().equals(player.getUniqueId())) {
                    Duration duration = Duration.between(meta.getCreated(), now);
                    String timeAgo = formatDuration(duration);
                    String timeformat;
                    if (lang.AgoParameter()) {
                       timeformat = String.format("%s %s", timeAgo, lang.translate("time.ago"));
                    } else
                        timeformat = String.format("%s %s", lang.translate("time.ago"), timeAgo);

                    player.sendMessage(String.format("[SimpleDeathChest] %d. X:%d, Y:%d, Z:%d (%s) (%s)",
                            i++, block.getX(), block.getY(), block.getZ(), timeformat, lang.translate("list.yours")));
                }
            }
            if (i == 1) {
                player.sendMessage(String.format("[SimpleDeathChest] %s", lang.translate("list.noDeathchests")));
            }
            return true;
        }
        player.sendMessage(String.format("[SimpleDeathChest] %s: /deathchest list", lang.translate("list.usage")));
        return true;
    }

    // Helper to format duration as '4 minutes', '1 day and 25 minutes', etc.
    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        if (days > 0) {
            return String.format("%d %s & %d %s",
                    days, days == 1 ? lang.translate("time.day") : lang.translate("time.days"),
                    minutes, minutes == 1 ? lang.translate("time.minute") : lang.translate("time.minutes"));
        } else if (hours > 0) {
            return String.format("%d %s & %d %s",
                    hours, hours == 1 ? lang.translate("time.hour") : lang.translate("time.hours"),
                    minutes, minutes == 1 ? lang.translate("time.minute") : lang.translate("time.minutes"));
        } else {
            return String.format("%d %s",
                    minutes, minutes == 1 ? lang.translate("time.minute") : lang.translate("time.minutes"));
        }
    }
}
