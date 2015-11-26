package me.koenn.kp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    //Variables:
    private Inventory serverSelectorInventory;

    private ItemStack[] servers = new ItemStack[6];

    //Constructor:
    public InventoryManager(){
        ItemStack prison = new ItemStack(Material.IRON_FENCE);
        ItemStack hub = new ItemStack(Material.NETHER_STAR);
        ItemStack minigames = new ItemStack(Material.BLAZE_ROD);
        ItemStack skyblock = new ItemStack(Material.GRASS);
        ItemStack factions = new ItemStack(Material.GOLDEN_APPLE);
        this.servers[0] = setData(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hub", ChatColor.GREEN + "Click to go to the Hub", hub);
        this.servers[1] = setData(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison", ChatColor.GRAY + "Click to go to the Prison server", prison);
        this.servers[2] = setData(ChatColor.GOLD + "" + ChatColor.BOLD + "Minigames", ChatColor.YELLOW + "Click to go to the Minigames server", minigames);
        this.servers[3] = setData(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Skyblock", ChatColor.GREEN + "Click to go to the Skyblock server", skyblock);
        this.servers[4] = setData(ChatColor.RED + "" + ChatColor.BOLD + "Factions", ChatColor.DARK_RED + "Click to go to the Factions server", factions);
    }

    private ItemStack setData(String n, String l, ItemStack i){
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        List<String> lr = new ArrayList<String>();
        lr.add(l);
        im.setLore(lr);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    public void openServerSelector(Player p){
        serverSelectorInventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.YELLOW + "" + ChatColor.BOLD + "Server Selector");
        for(ItemStack i : servers){
            serverSelectorInventory.addItem(i);
        }
    }


}
