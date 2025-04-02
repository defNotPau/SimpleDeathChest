package me.pau.plugins;

import me.pau.plugins.handlers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    public static DeathChest instance; 
    
    Death death;
    Interaction interaction;
    Chests chests;

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        chests = new Chests(this);
        chests.load();

        death = new Death(this, chests);
        interaction = new Interaction(this, chests);

        chests.restoreInWorld();
    }

    @Override
    public void onDisable() {
        chests.save();
        warnPrint("I'm def NOT working rn");
    }

    static public void infoPrint(String msg) {
        instance.getLogger().info(msg);
    }
    static public void warnPrint(String msg) {
        instance.getLogger().warning(msg);
    }
}

