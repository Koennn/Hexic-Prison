package me.koenn.kp;

import org.bukkit.DyeColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class Lapis implements Listener {

    private ItemStack lapis;

    public Lapis() {
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        this.lapis = d.toItemStack();
        this.lapis.setAmount(64);
    }

    @EventHandler
    public void openInventoryEvent(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, this.lapis);
        }
    }

    @EventHandler
    public void closeInventoryEvent(InventoryCloseEvent e) {
        e.getInventory().setItem(1, null);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        if ((e.getInventory()) instanceof EnchantingInventory) {
            if (e.getSlot() == 1) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void enchantItemEvent(EnchantItemEvent e) {
        e.getInventory().setItem(1, this.lapis);
    }
}