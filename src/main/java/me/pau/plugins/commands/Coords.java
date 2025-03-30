package me.pau.plugins.commands;

import me.pau.plugins.handlers.Chests;
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

    public Coords(Chests deathChests) {
        this.deathChests = deathChests;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.translatable("commands.coords.not_player", "YOU'RE NOT A PLAYER >:("));
            return true;
        }

        Player player = (Player) sender;

        Location location = deathChests.get(player.getName());
        if (location == null) {
            player.sendMessage(Component.translatable("commands.coords.not_died", "You haven't died yet :D"));
            return true;
        }

        int x = location.getBlockX();
        int z = location.getBlockZ();
        int y = location.getBlockY();

        Component staticComponent = Component.translatable("commands.coords.pre-copyable", "Your latest chest coordinates are: ");
        Component copyableComponent = Component.text((x + ", " + y + ", " + z), NamedTextColor.GREEN, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.copyToClipboard(x + " " + y + " " + z));

        player.sendMessage(staticComponent.append(copyableComponent));
        return true;
    }
}
