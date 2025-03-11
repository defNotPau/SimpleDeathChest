package me.pau.plugins;
import me.pau.plugins.commands.CoordsCommand;
import me.pau.plugins.handlers.*;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    DeathHandler deathHandler;
    ChestInteractionHandler chestInteractionHandler;
    DeathChestsHandler saveHandler;
    RestoreHandler restoreHandler;

    Utils utils;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        boolean isCoordsCommandEnabled = this.getConfig().getBoolean("commands.coords", true);

        utils = new Utils(this);

        saveHandler = new DeathChestsHandler(this);
        saveHandler.load();

        deathHandler = new DeathHandler(this, saveHandler);
        chestInteractionHandler = new ChestInteractionHandler(this, saveHandler, utils);

        restoreHandler = new RestoreHandler(utils, saveHandler);
        restoreHandler.restore();

        utils.infoPrint("I might be working");

        PluginCommand coordsCommand = getCommand("coords");
        if (coordsCommand != null) {
            coordsCommand.setExecutor(new CoordsCommand(saveHandler, isCoordsCommandEnabled));
        } else {
            utils.severePrint("Failed to register /coords command. Check your plugin.yml or Paper configuration.");
        }
    }

    @Override
    public void onDisable() {
        saveHandler.save();
        utils.warnPrint("I'm def NOT working rn");
    }
}

