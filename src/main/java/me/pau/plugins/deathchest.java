package me.pau.plugins;
import me.pau.plugins.handlers.ChestInteractionHandler;
import me.pau.plugins.handlers.DeathHandler;

import me.pau.plugins.handlers.DeathChestsHandler;
import me.pau.plugins.handlers.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class deathchest extends JavaPlugin {
    DeathHandler deathHandler;
    ChestInteractionHandler chestInteractionHandler;
    DeathChestsHandler saveHandler;

    Utils utils;

    @Override
    public void onEnable() {
        deathHandler = new DeathHandler(this);
        chestInteractionHandler = new ChestInteractionHandler(this);

        saveHandler = new DeathChestsHandler(this);
        saveHandler.load();

        utils = new Utils(this);
        utils.infoPrint("I might be working");
    }

    @Override
    public void onDisable() {
        saveHandler.save();

        utils.warnPrint("I'm def NOT working rn");
    }
}

