package me.pau.plugins;
import me.pau.plugins.handlers.DeathHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class deathchest extends JavaPlugin {

    @Override
    public void onEnable() {
        DeathHandler deathHandler = new DeathHandler(this);

        this.getLogger().info("I might be working");
        deathHandler.loadDeathChests();
    }

    @Override
    public void onDisable() {
        this.getLogger().warning("I'm def NOT working rn");
    }
}
