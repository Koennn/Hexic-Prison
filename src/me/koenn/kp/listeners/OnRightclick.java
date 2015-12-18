package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OnRightclick implements Listener {

    private Main main;

    public OnRightclick(Main main) {
        this.main = main;
    }

    private ItemStack serverSelector() {
        ItemStack i = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Server Selector");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click to select a server!");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    private ItemStack helpMenu(){
        ItemStack i = new ItemStack(Material.DIAMOND);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Help Menu");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click open the help menu");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    private ItemStack serverInfo(){
        ItemStack i = new ItemStack(Material.EMERALD);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Server Info");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click see server information");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    @EventHandler
    public void onRightclick(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if(p.getInventory().getItemInHand().equals(serverSelector())) {
                e.setCancelled(true);
                main.ivm.openServerSelector(p);
            }
            if(p.getInventory().getItemInHand().equals(helpMenu())) {
                e.setCancelled(true);
                p.performCommand("help");
            }
            if(p.getInventory().getItemInHand().equals(serverInfo())) {
                e.setCancelled(true);
                main.ShowServerInfo(p);
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}