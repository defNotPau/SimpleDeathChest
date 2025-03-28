package me.pau.plugins.deathchest.commands;

import me.pau.plugins.deathchest.handlers.Chests;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Coords implements CommandExecutor {
    Chests deathChests;
    boolean enabled;

    public Coords(Chests deathChests, boolean status) {
        this.enabled = status;
        this.deathChests = deathChests;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!enabled) {
            sender.sendMessage(Component.text("Uhhh... this is not enabled ;c \n (if you think this is an error you should ask an admin...)"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("YOU'RE NOT A PLAYER >:(", NamedTextColor.DARK_AQUA));
            return true;
        }

        Location location = deathChests.get(player.getName());
        if (location == null) {
            player.sendMessage(Component.text("You haven't died yet :D"));
            return true;
        }

        int x = location.getBlockX();
        int z = location.getBlockZ();
        int y = location.getBlockY();

        Component staticComponent = Component.text("Your latest chest's coordinates are: ", NamedTextColor.BLUE);
        Component copyableComponent = Component.text((x + ", " + y + ", " + z), NamedTextColor.GREEN, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.copyToClipboard(x + " " + y + " " + z));

        player.sendMessage(staticComponent.append(copyableComponent));
        return true;
    }
}
