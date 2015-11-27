package me.koenn.kp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu implements Listener {

    private ItemStack[] settings;
    //private ItemStack[] adminSettings;
    //private HashMap<Player, Boolean> admin = new HashMap<Player, Boolean>();
    private ArrayList<Player> hide = new ArrayList<>();

    private Main main;

    public String textColor = ChatColor.GREEN + "";
    public String titleColor = ChatColor.YELLOW + "" + ChatColor.BOLD;

    public SettingsMenu(Main m){
        this.main = m;
    }

    private void initSettings(Player p){
        this.settings = new ItemStack[2];

        String m;

        //CHAT MODE:
        ItemStack i1 = new ItemStack(Material.PAPER, 1);
        ItemMeta im1 = i1.getItemMeta();
        im1.setDisplayName(titleColor + "Chat mode");
        im1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> l1 = new ArrayList<String>();
        if(main.mode.get(p) == "global"){
            m = "Server";
            im1.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        } else {
            m = "Global";
        }
        l1.add(textColor + "Click to change to " + titleColor + m + textColor + " mode.");
        im1.setLore(l1);
        i1.setItemMeta(im1);

        settings[0] = i1;

        //SHOW PLAYERS
        ItemStack i2 = new ItemStack(Material.EYE_OF_ENDER, 1);
        ItemMeta im2 = i2.getItemMeta();
        im2.setDisplayName(titleColor + "Hide players");
        im2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> l2 = new ArrayList<String>();
        if(!(this.hide.contains(p))){
            m = "Hide";
        } else {
            m = "Show";
            im2.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        }
        l2.add(textColor + "Click to " + titleColor + m + textColor + " players.");
        im2.setLore(l2);
        i2.setItemMeta(im2);

        settings[1] = i2;


    }

    //private void initAdminSettings(Player p){

    //}

    public void openMenu(Player p){
        p.openInventory(initMenu(p));
    }

    private Inventory initMenu(Player p){
        Inventory settingsMenu = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "" + ChatColor.BOLD +  "Settings");
        initSettings(p);
        //initAdminSettings(p);
        for (ItemStack setting : settings) {
            settingsMenu.addItem(setting);
        }
        return settingsMenu;
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        try{
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getName().contains(ChatColor.YELLOW + "" + ChatColor.BOLD + "Settings")){
                e.setCancelled(true);
                if (e.getSlot() == 0) {
                    p.performCommand("chat");
                    main.spam.remove(p);
                } else if (e.getSlot() == 1) {
                    if (hide.contains(p)) {
                        Bukkit.getServer().getOnlinePlayers().forEach(p::showPlayer);
                        p.sendMessage(textColor + "All players are now " + titleColor + "Visible.");
                        hide.remove(p);
                    } else {
                        Bukkit.getServer().getOnlinePlayers().forEach(p::hidePlayer);
                        p.sendMessage(textColor + "All players are now " + titleColor + "Hidden.");
                        hide.add(p);
                    }
                }
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5F, 0.2F);
                p.openInventory(initMenu(p));
            }
        } catch(NullPointerException ex){
            Bukkit.getServer().getLogger().severe("");
            Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING" + e.getEventName() +  "BY PLAYER " + e.getWhoClicked().getName() + ":");
            Bukkit.getServer().getLogger().severe("");
            ex.printStackTrace();
            e.getWhoClicked().sendMessage(ChatColor.RED + "An error occurred while doing this. Please contact Koenn.");
        }
    }
}