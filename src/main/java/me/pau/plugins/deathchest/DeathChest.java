package me.pau.plugins.deathchest;

import me.pau.plugins.deathchest.commands.ChestList;
import me.pau.plugins.deathchest.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class DeathChest extends JavaPlugin {
    public static DeathChest instance;

    // Integrations' variables
    private boolean isExcellentEnchantsEnabled = false;

    // Config variables
    static public String language;

    static public boolean playerBreakable;
    static public boolean dropItemsWhenBroken;
    static public boolean explosionProof;
    static public boolean dropItemsWhenExploded;

    static public boolean nameVisible;

    // Other classes that should be summoned
    Death death;
    Interaction interaction;
    Chests chests;
    Lang lang;

    @Override
    public void onEnable() {
        instance = this;
        infoPrint("I might be working");

        // Config.yml stuff
        instance.saveDefaultConfig();
        language = this.getConfig().getString("general.language", "en");

        playerBreakable = this.getConfig().getBoolean("chest_interactions.player_breakable", false);
        explosionProof = this.getConfig().getBoolean("chest_interactions.explosion_proof", true);
        dropItemsWhenExploded = this.getConfig().getBoolean("chest_interactions.items_drop_when_exploded", true);
        dropItemsWhenBroken = this.getConfig().getBoolean("chest_interactions.items_drop_when_broken", true);

        nameVisible = this.getConfig().getBoolean("chest_customization.name_on_chest", true);

        // Anything to do with integrations
        isExcellentEnchantsEnabled = getServer().getPluginManager().isPluginEnabled("ExcellentEnchants");

        // Chests class summoning
        chests = new Chests(instance);
        chests.load();

        // If there are any chests loaded onEnable, log the count
        if (!chests.getAllBlocks().isEmpty()) {
            infoPrint("There are " + chests.getAllBlocks().size() + " death chests loaded in the world.");
        }

        death = new Death(instance, chests);
        interaction = new Interaction(instance, chests);
        lang = new Lang(instance);
        chests.restoreInWorld();

        // Register command
        Objects.requireNonNull(getCommand("deathchest")).setExecutor(new ChestList(chests, lang));
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
     * IMPORTANT,
     * STOPS THE PLUGIN
     * COMPLETELY
     *
     * @param msg message to be logged as ERROR on the server's console as the plugin shuts down
     */
    static public void error(String msg) {
        instance.getLogger().severe(msg);
        instance.getLogger().warning("Plugin is shutting down due to a critical error");
        instance.getLogger().info("(Ignore error below that says \"The plugin classloader for DeathChest has thrown a zip file error\")");
        Bukkit.getPluginManager().disablePlugin(instance);
    }

    /**
     * @return what does the name of this function say?
     */
    public boolean isExcellentEnchantsEnabled() {
        return isExcellentEnchantsEnabled;
    }
}
