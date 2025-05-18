package me.pau.plugins.deathchest;

import me.pau.plugins.deathchest.handlers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathChest extends JavaPlugin {
    public static DeathChest instance;

    // Integrations' variables
    private boolean isExcellentEnchantsEnabled = false;

    // Config variables
    static public boolean playerBreakable;
    static public boolean explosionProof;

    // Other classes that should be summoned
    Death death;
    Interaction interaction;
    Chests chests;

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        // Config.yml stuffs
        instance.saveDefaultConfig();
        playerBreakable = this.getConfig().getBoolean("chest_interactions.player_breakable", false);
        explosionProof = this.getConfig().getBoolean("chest_interactions.explosion_proof", true);

        // Anything to do with integrations such as variables checking if a plugin is enabled
        isExcellentEnchantsEnabled = getServer().getPluginManager().isPluginEnabled("ExcellentEnchants");

        // Main class summoning
        chests = new Chests(instance);
        chests.load();
        death = new Death(instance, chests);

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

    /**
     * @return what does the name of this function say?
     */
    public boolean isExcellentEnchantsEnabled() {
        return isExcellentEnchantsEnabled;
    }
}
