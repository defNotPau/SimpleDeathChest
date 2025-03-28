package me.pau.plugins.deathchest;
import me.pau.plugins.deathchest.commands.Coords;
import me.pau.plugins.deathchest.handlers.*;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    Death deathHandler;
    Interaction chestInteraction;
    Chests saveHandler;
    Restore restoreHandler;

    private final static DeathChest instance = new DeathChest();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        boolean isCoordsCommandEnabled = this.getConfig().getBoolean("commands.coords", true);

        saveHandler = new Chests(this);
        saveHandler.load();

        deathHandler = new Death(this, saveHandler);
        chestInteraction = new Interaction(this, saveHandler);

        restoreHandler = new Restore(saveHandler);
        restoreHandler.restore();

        infoPrint("I might be working");

        PluginCommand coordsCommand = getCommand("coords");
        if (coordsCommand != null) {
            coordsCommand.setExecutor(new Coords(saveHandler, isCoordsCommandEnabled));
        } else {
            severePrint("Failed to register /coords command. Check your plugin.yml or Paper configuration.");
        }
    }

    @Override
    public void onDisable() {
        saveHandler.save();
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

