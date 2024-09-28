package me.pau.plugins;
import me.pau.plugins.handlers.DeathHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class deathchest extends JavaPlugin {

    DeathHandler deathHandler;

    @Override
    public void onEnable() {
        deathHandler = new DeathHandler(this);

        this.getLogger().info("I might be working");
        deathHandler.loadDeathChests();
    }

    @Override
    public void onDisable() {
        this.getLogger().warning("I'm def NOT working rn");

        deathHandler.saveDeathChests();
    }
}
