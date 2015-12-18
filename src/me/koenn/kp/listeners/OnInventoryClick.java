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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OnInventoryClick implements Listener {

    private Main main;

    public OnInventoryClick(Main main) {
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
    public void onInventoryClick(InventoryClickEvent e) {
        try {
            Player p = (Player) e.getWhoClicked();
            if(!(e.getCurrentItem() == (null) && !(e.getCursor() == (null)))){
                if (e.getCurrentItem().equals(serverSelector())){
                    main.ivm.openServerSelector(p);
                    e.setCancelled(true);
                }
                if (e.getCursor().equals(serverSelector())){
                    main.ivm.openServerSelector(p);
                    e.setCancelled(true);
                }
                if(e.getCurrentItem().equals(helpMenu())){
                    e.setCancelled(true);
                    p.performCommand("help");
                }
                if(e.getCurrentItem().equals(helpMenu())){
                    e.setCancelled(true);
                    p.performCommand("help");
                }
                if(e.getCurrentItem().equals(serverInfo())) {
                    e.setCancelled(true);
                    main.ShowServerInfo(p);
                }
                if(e.getCursor().equals(serverInfo())) {
                    e.setCancelled(true);
                    main.ShowServerInfo(p);
                }
                if(e.getInventory().getName().contains("Information about ")){
                    e.setCancelled(true);
                    if(e.getSlot() == 4){
                        p.teleport(main.ivm.history.get(p));
                        MessageManager.getInstance().msg(e.getWhoClicked(), MessageManager.MessageType.WARN, "Teleporting to " + main.ivm.history.get(p).getName());
                    }
                }
                if (e.getInventory().getName().contains(main.ivm.getServerSelector())){
                    e.setCancelled(true);
                    switch(e.getSlot()){
                        case 0:
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Hub");
                            break;
                        case 1:
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Prison");
                            break;
                        case 2:
                            if((p.isOp())) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Minigames");
                            } else {
                                p.sendMessage(ChatColor.RED + "This server is not open yet!");
                            }
                            break;
                        case 3:
                            if((p.isOp())) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Skyblock");
                            } else {
                                p.sendMessage(ChatColor.RED + "This server is not open yet!");
                            }
                            break;
                        case 4:
                            if((p.isOp())) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Factions");
                            } else {
                                p.sendMessage(ChatColor.RED + "This server is not open yet!");
                            }
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            main.catchEvent(ex, (Player) e.getWhoClicked(), e.getEventName());
        }
    }

}