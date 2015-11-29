package me.koenn.kp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    //Variables:
    private Inventory serverSelectorInventory;

    private ItemStack[] servers = new ItemStack[6];

    private Plugin plugin;

    public String getServerSelector(){
        return serverSelectorInventory.getName();
    }

    //Constructor:
    public InventoryManager(Plugin p){
        plugin = p;
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
        Integer index = 0;
        serverSelectorInventory = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "" + ChatColor.BOLD + "Server Selector");
        for(ItemStack i : servers){
            serverSelectorInventory.setItem(index, i);
            index++;
        }
    }

    private ItemStack setData(String n, String l, ItemStack i){
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        List<String> lr = new ArrayList<String>();
        lr.add(l);
        im.setLore(lr);
        //im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        //im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    public void openServerSelector(Player p){
        p.openInventory(serverSelectorInventory);
    }
}
