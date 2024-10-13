package me.pau.plugins.handlers;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RestoreHandler {
    Utils utils;
    DeathChestsHandler deathChests;

    public RestoreHandler(Utils utils, DeathChestsHandler deathChests) {
        this.utils = utils;
        this.deathChests = deathChests;
    }

    public void restore() {
        for (Block chest : deathChests.keySet()) {
            if (chest != null && chest.getLocation().getChunk().isLoaded()) {
                chest.setType(Material.CHEST);
            } else {
                utils.warnPrint("Skipped restoring chest at unloaded chunk: " + chest.getLocation());
            }
        }

        utils.infoPrint("Death Chest restore complete");
    }
}

