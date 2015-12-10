package me.koenn.kp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryManager {

    //Variables:
    private Inventory serverSelectorInventory;
    private Inventory playerInfo;

    public HashMap<Player, Player> history = new HashMap<>();

    private ItemStack[] servers = new ItemStack[6];

    private Plugin plugin;
    private Main m;

    public String getInfoThingy(){
        return playerInfo.getName();
    }
    public String getServerSelector(){
        return serverSelectorInventory.getName();
    }
    public ItemStack prison;

    //Constructor:
    public InventoryManager(Plugin p){
        plugin = p;
        prison = new ItemStack(Material.IRON_FENCE);
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
        serverSelectorInventory.setItem(1, setData(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison", ChatColor.GRAY + "Click to go to the Prison server", prison));
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
        InventoryManager ivm = new InventoryManager(plugin);
        prison = new ItemStack(Material.IRON_FENCE);
        serverSelectorInventory.setItem(1, setData(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison", ChatColor.GRAY + "Click to go to the Prison server", prison));
        p.openInventory(serverSelectorInventory);
    }

    public Inventory playerInfoInit(Player p, Player s){
        history.remove(s);
        history.put(s, p);
        playerInfo = Bukkit.createInventory(null, 9, "Information about " + p.getName());
        ItemStack i = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + p.getName() + "'s warnings");
        List<String> l = new ArrayList<>();
        try{
            l.add(ChatColor.DARK_AQUA + p.getName() + " got warned " + m.warns.get(p) + " times!");
        } catch (Exception ex){
            l.add(ChatColor.DARK_AQUA + p.getName() + " got warned 0 times!");
        }
        im.setLore(l);
        i.setItemMeta(im);
        playerInfo.setItem(2, i);
        l.clear();
        i = new ItemStack(Material.ENDER_PEARL, 1);
        im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Teleport to " + p.getName());
        l.add(ChatColor.DARK_AQUA + "Click to teleport to " + p.getName());
        im.setLore(l);
        i.setItemMeta(im);
        playerInfo.setItem(4, i);
        l.clear();
        i = new ItemStack(Material.BARRIER, 1);
        im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + p.getName() + "'s mutes");
        try{
            l.add(ChatColor.DARK_AQUA + p.getName() + " got muted " + m.mutes.get(p) + " times!");
        } catch (Exception ex){
            l.add(ChatColor.DARK_AQUA + p.getName() + " got muted 0 times!");
        }
        im.setLore(l);
        i.setItemMeta(im);
        playerInfo.setItem(6, i);
        l.clear();
        return playerInfo;
    }
}
