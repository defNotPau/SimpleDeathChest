package me.pau.plugins;
import me.pau.plugins.handlers.ChestInteractionHandler;
import me.pau.plugins.handlers.DeathHandler;

import me.pau.plugins.handlers.SaveHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class deathchest extends JavaPlugin {

    DeathHandler deathHandler;
    ChestInteractionHandler chestInteractionHandler;

    SaveHandler saveHandler;

    @Override
    public void onEnable() {
        deathHandler = new DeathHandler(this);
        chestInteractionHandler = new ChestInteractionHandler(this);

        saveHandler = new SaveHandler(this);
        saveHandler.loadDeathChests();

        this.getLogger().info("I might be working");
    }

    @Override
    public void onDisable() {
        saveHandler.saveDeathChests();

        this.getLogger().warning("I'm def NOT working rn");
    }
}

