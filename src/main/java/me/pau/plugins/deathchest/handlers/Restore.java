package me.pau.plugins.deathchest.handlers;

import org.bukkit.Material;
import org.bukkit.block.Block;

import static me.pau.plugins.deathchest.DeathChest.infoPrint;
import static me.pau.plugins.deathchest.DeathChest.warnPrint;

public class Restore {
    Chests deathChests;

    public Restore(Chests deathChests) {
        this.deathChests = deathChests;
    }

    public void restore() {
        for (Block chest : deathChests.keySet()) {
            if (chest != null && chest.getLocation().getChunk().isLoaded()) {
                chest.setType(Material.CHEST);
            } else {
                assert chest != null;
                warnPrint("Skipped restoring chest at unloaded chunk: " + chest.getLocation());
            }
        }

        infoPrint("Death Chest restore complete");
    }
}

