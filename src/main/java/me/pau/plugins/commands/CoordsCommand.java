package me.pau.plugins.commands;

import me.pau.plugins.handlers.DeathChestsHandler;
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

public class CoordsCommand implements CommandExecutor {
    DeathChestsHandler deathChests;

    public CoordsCommand(DeathChestsHandler deathChests) {
        this.deathChests = deathChests;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("YOU'RE NOT A PLAYER >:(", NamedTextColor.DARK_AQUA));
            return true;
        }

        Player player = (Player) sender;

        Location location = deathChests.get(player.getName());
        if (location == null) {
            player.sendMessage(Component.text("You haven't died yet :D"));
            return true;
        }

        int x = location.getBlockX();
        int z = location.getBlockZ();
        int y = location.getBlockY();

        Component staticComponent = Component.text("Your latest chest's coordinates are: ", NamedTextColor.BLUE);
        Component copyableComponent = Component.text((x + ", " + y + ", " + z), NamedTextColor.DARK_BLUE, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.copyToClipboard(x + " " + y + " " + z));

        player.sendMessage(staticComponent.append(copyableComponent));
        return true;
    }
}
