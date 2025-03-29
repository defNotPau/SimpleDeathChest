package me.pau.plugins.deathchest;
import me.pau.plugins.deathchest.commands.Coords;
import me.pau.plugins.deathchest.handlers.*;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    Death deathHandler;
    Interaction chestInteraction;
    Chests chests;

    private final static DeathChest instance = new DeathChest();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        boolean isCoordsCommandEnabled = this.getConfig().getBoolean("commands.coords", true);

        chests = new Chests(this);
        chests.load();

        deathHandler = new Death(this, chests);
        chestInteraction = new Interaction(this, chests);
        
        infoPrint("I might be working");
        chests.restore();

        PluginCommand coordsCommand = getCommand("coords");
        if (coordsCommand != null && isCoordsCommandEnabled) {
            coordsCommand.setExecutor(new Coords(chests));
        } else {
            severePrint("Failed to register /coords command. Check your plugin.yml or Paper configuration.");
        }
    }

    @Override
    public void onDisable() {
        chests.save();
        warnPrint("I'm def NOT working rn");
    }

    public static DeathChest getInstance() {
        return instance;
    }

    public static void infoPrint(String msg) {
        instance.getLogger().info(msg);
    }
    public static void warnPrint(String msg) {
        instance.getLogger().warning(msg);
    }
    public static void severePrint(String msg) {
        instance.getLogger().severe(msg);
    }
}

