package me.pau.plugins.deathchest.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.pau.plugins.deathchest.DeathChest.nameVisible;

import java.time.Instant;
import java.util.UUID;

public class ChestMeta {
    private final Inventory inventory;
    private final String ownerName;

    // UUID of the player this chest belongs to
    private final UUID owner;
    private final Instant created;

    public ChestMeta(int size, String ownerName, UUID owner, Instant created) {
        this.ownerName = ownerName;
        this.owner = owner;
        this.inventory = (nameVisible)
                ? Bukkit.createInventory(null, size, ownerName)
                : Bukkit.createInventory(null, size);
        this.created = created;
    }

    public ChestMeta(int size, Player owner) {
        this(size, owner.getName(), owner.getUniqueId(), Instant.now());
    }

    public UUID getOwner() {
        return owner;
    }

    public Instant getCreated() {
        return created;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void addItem(ItemStack item) {
        inventory.addItem(item);
    }

    public void setContents(ItemStack[] items) {
        inventory.setContents(items);
    }
}
