package me.koenn.kp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryManager {

    public HashMap<Player, Player> history = new HashMap<>();
    public ArrayList<Player> h = new ArrayList<>();
    public ItemStack prison;
    Inventory playerInfo;
    //Variables:
    private Inventory serverSelectorInventory;
    private Inventory warps;
    private ItemStack[] servers = new ItemStack[6];
    private Plugin plugin;
    private Main main;

    //Constructor:
    public InventoryManager(Plugin p, Main m) {
        this.main = m;
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
        for (ItemStack i : servers) {
            serverSelectorInventory.setItem(index, i);
            index++;
        }
        serverSelectorInventory.setItem(1, setData(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison", ChatColor.GRAY + "Click to go to the Prison server", prison));

        this.warps = Bukkit.createInventory(null, 27, "Warps");
    }

    public String getServerSelector() {
        return serverSelectorInventory.getName();
    }

    public Integer getWarns(Player p) {
        ConfigurationSection w = main.getConfig().getConfigurationSection("warns");
        Integer a;
        try {
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex) {
            w.createSection(p.getUniqueId().toString());
            a = 0;
            main.log("Failed to load warns for player... Setting value to 0.");
        }
        return a;
    }

    public Integer getMutes(Player p) {
        ConfigurationSection w = main.getConfig().getConfigurationSection("mutes");
        Integer a;
        try {
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex) {
            w.createSection(p.getUniqueId().toString());
            a = 0;
            main.log("Failed to load mutes for player... Setting value to 0.");
        }
        return a;
    }

    private ItemStack setData(String n, String l, ItemStack i) {
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(n);
        List<String> lr = new ArrayList<String>();
        lr.add(l);
        im.setLore(lr);
        i.setItemMeta(im);
        return i;
    }

    public void openServerSelector(Player p) {
        InventoryManager ivm = new InventoryManager(plugin, main);
        prison = new ItemStack(Material.IRON_FENCE);
        serverSelectorInventory.setItem(1, setData(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison", ChatColor.GRAY + "Click to go to the Prison server", prison));
        p.openInventory(serverSelectorInventory);
    }

    public Inventory playerInfoInit(Player p, Player s) {
        history.remove(s);
        history.put(s, p);
        h.add(s);
        playerInfo = Bukkit.createInventory(null, 9, "Info about " + p.getName());
        ItemStack i = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + p.getName() + "'s warnings");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.DARK_AQUA + p.getName() + " got warned " + getWarns(p).toString() + " times!");
        im.setLore(l);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        i.setItemMeta(im);
        playerInfo.setItem(2, i);
        l.clear();
        i = new ItemStack(Material.ENDER_PEARL, 1);
        im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Teleport to " + p.getName());
        l.add(ChatColor.DARK_AQUA + "Click to teleport to " + p.getName());
        im.setLore(l);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        i.setItemMeta(im);
        playerInfo.setItem(4, i);
        l.clear();
        i = new ItemStack(Material.BARRIER, 1);
        im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + p.getName() + "'s mutes");
        l.add(ChatColor.DARK_AQUA + p.getName() + " got muted " + getMutes(p) + " times!");
        im.setLore(l);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        i.setItemMeta(im);
        playerInfo.setItem(6, i);
        l.clear();
        return playerInfo;
    }

    private void initWarps() {
        this.warps.clear();
        ConfigurationSection warps = main.getConfig().getConfigurationSection("warps");
        ArrayList<ItemStack> allwarps = new ArrayList<>();
        if (warps.get("enabled").toString().equalsIgnoreCase("true")) {
            warps.getKeys(false).stream().filter(x -> !(x.contains("enabled"))).forEach(x -> {
                ItemStack it = new ItemStack(Material.valueOf(x));
                ItemMeta im = it.getItemMeta();
                im.setDisplayName(main.titleColor + warps.getConfigurationSection(x).get("name").toString());
                List<String> l = new ArrayList<>();
                l.add(main.textColor + "Click to warp to " + warps.getConfigurationSection(x).get("name").toString());
                im.setLore(l);
                it.setItemMeta(im);
                allwarps.add(it);
            });
            allwarps.forEach(this.warps::addItem);
        } else {
            main.log("Warps are disabled in config");
        }
    }

    public void openWarps(Player p) {
        initWarps();
        p.closeInventory();
        p.openInventory(warps);
    }
}
