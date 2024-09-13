package me.pau.plugins;
import me.pau.plugins.handlers.DeathHandler;

import org.bukkit.plugin.java.JavaPlugin;

public final class deathchest extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("I might be working");

        new DeathHandler(this);
    }

    @Override
    public void onDisable() {
        getLogger().warning("I'm def NOT working rn");
    }
}
