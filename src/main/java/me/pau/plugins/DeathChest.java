package me.pau.plugins;
import me.pau.plugins.commands.Coords;
import me.pau.plugins.handlers.*;

import org.bukkit.command.PluginCommand;
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
        
        this.saveDefaultConfig();
        boolean isCoordsCommandEnabled = this.getConfig().getBoolean("commands.coords", true);

        chests = new Chests(this);
        chests.load();

        death = new Death(this, chests);
        interaction = new Interaction(this, chests);

        chests.restoreInWorld();

        PluginCommand coordsCommand = getCommand("coords");
        if (coordsCommand != null) {
            if (isCoordsCommandEnabled) {
                coordsCommand.setExecutor(new Coords(chests));
                infoPrint("/coords enabled");
            } else {
                warnPrint("/coords disabled");
            }
        } else {
            severePrint("Failed to register /coords command");
        }
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
    static public void severePrint(String msg) {
        instance.getLogger().severe(msg);
    }
}

