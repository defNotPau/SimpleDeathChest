package me.pau.plugins.deathchest.handlers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.pau.plugins.deathchest.DeathChest.nameVisible;

import java.time.Instant;
import java.util.UUID;

public class ChestMeta {
    private final Inventory inventory;
    private final String ownerName;
    private final UUID owner;
    private final Instant created;

    @SuppressWarnings("deprecation")
    public ChestMeta(int size, String ownerName) {
        this.ownerName = ownerName;
        this.inventory = (nameVisible)
                ? Bukkit.createInventory(null, size, ownerName)
                : Bukkit.createInventory(null, size);

        this.owner = Bukkit.getOfflinePlayer(ownerName).getUniqueId();
        this.created = Instant.now();
    }

    @SuppressWarnings("deprecation")
    public ChestMeta(int size, String ownerName, Instant instant) {
        this.ownerName = ownerName;
        this.inventory = (nameVisible)
                ? Bukkit.createInventory(null, size, ownerName)
                : Bukkit.createInventory(null, size);

        this.owner = Bukkit.getOfflinePlayer(ownerName).getUniqueId();
        this.created = instant;
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
