package me.pau.plugins.deathchest;

import me.pau.plugins.deathchest.handlers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    public static DeathChest instance;
    static public boolean playerBreakable;
    static public boolean explosionProof;

    Death death;
    Interaction interaction;
    Chests chests;

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        chests = new Chests(instance);
        chests.load();
        death = new Death(instance, chests);

        playerBreakable = this.getConfig().getBoolean("chest_interactions.player_breakable", false);
        explosionProof = this.getConfig().getBoolean("chest_interactions.explosion_proof", true);
        interaction = new Interaction(instance, chests);

        chests.restoreInWorld();
    }

    @Override
    public void onDisable() {
        chests.save();
        warnPrint("I'm def NOT working rn");
    }

    /**
     * @param msg message to be logged as INFO on the server's console
     */
    static public void infoPrint(String msg) {
        instance.getLogger().info(msg);
    }

    /**
     * @param msg message to be logged as WARN on the server's console
     */
    static public void warnPrint(String msg) {
        instance.getLogger().warning(msg);
    }
}

