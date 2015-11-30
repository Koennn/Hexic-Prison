package me.koenn.kp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.server.v1_8_R3.ExceptionEntityNotFound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Main extends JavaPlugin implements Listener {

    //Variables:
    HashMap<Player, Boolean> irc = new HashMap<>();
    HashMap<Player, Boolean> mute = new HashMap<>();
    HashMap<Player, String> spam = new HashMap<>();
    HashMap<Player, String> realm = new HashMap<>();
    HashMap<Player, String> rank = new HashMap<>();
    HashMap<Player, String> nick = new HashMap<>();
    public HashMap<Player, String> mode = new HashMap<>();
    ArrayList<Player> change = new ArrayList<Player>();

    public String[] realms;

    public InventoryManager ivm;
    public SettingsMenu settings;

    private ArrayList<String> serverInfo = new ArrayList<String>();

    //OnEnable:
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        this.ivm = new InventoryManager(this);
        this.settings = new SettingsMenu(this);

        serverInfo.add(ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "+---------------------------------------+");
        serverInfo.add("                          " + settings.titleColor + "" + ChatColor.UNDERLINE + "SERVER INFO");
        serverInfo.add("");
        serverInfo.add(ChatColor.RED + "● " + settings.textColor + "More information coming soon!");
        serverInfo.add(ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "+---------------" + ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.WHITE + "" + ChatColor.BOLD + " Info " + ChatColor.GRAY + "" + ChatColor.BOLD + "]" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "------------------+");

        Bukkit.getServer().getPluginManager().registerEvents(settings, this);

        if(!(getConfig().contains("realms"))){
            getConfig().options().copyDefaults(true);
        }

        if(!(getConfig().contains("ranks"))){
            getConfig().createSection("ranks");
        }
        if(!(getConfig().contains("nicknames"))){
            getConfig().createSection("nicknames");
        }
        saveConfig();
        Integer i = 0;
        for (Map.Entry<String, Object> entry : getConfig().getConfigurationSection("realms").getValues(false).entrySet()){
            if(entry.getValue() != null){
                i++;
            }
        }
        this.realms = new String[i];
        i = 0;
        for (Map.Entry<String, Object> entry : getConfig().getConfigurationSection("realms").getValues(false).entrySet()){
            realms[i] = entry.getKey();
        }
    }

    //Command Handler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        try {
            //Universal commands:
            if (cmd.getName().equalsIgnoreCase("mute")) {
                if (checkPlayer(args, 0, 1, sender)) {
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    if (mute.containsKey(p)) {
                        mute.remove(p);
                        p.sendMessage(ChatColor.DARK_GRAY + "Player" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.DARK_GRAY + " was unmuted by" + ChatColor.YELLOW + " " + ChatColor.BOLD + sender.getName());
                    } else {
                        mute.put(p, true);
                        p.sendMessage(ChatColor.DARK_GRAY + "Player" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.DARK_GRAY + " was muted by" + ChatColor.YELLOW + " " + ChatColor.BOLD + sender.getName());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + cmd.getUsage());
                }
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("relog")){
                if(args[0] == "all"){
                    for(Player p : Bukkit.getServer().getOnlinePlayers()){
                        p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
                    }
                } else {
                    if (checkPlayer(args, 0, 1, sender)) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + cmd.getUsage());
                        return true;
                    }
                }
            }

            if(cmd.getName().equalsIgnoreCase("gotoserver")){
                if (checkPlayer(args, 0, 2, sender)){
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    p.closeInventory();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + p.getName() + " " + args[1]);
                    ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "Welcome to the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Hexic " + args[1] + ChatColor.GREEN + " Server!");
                    if(args[1].contains(getConfig().get("defaultRealm").toString())){
                        Bukkit.getLogger().severe(getConfig().get("defaultRealm").toString());
                        p.getInventory().setHeldItemSlot(4);
                        p.getInventory().setItemInHand(serverSelector());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + cmd.getUsage());
                    return true;
                }
            }

            if (sender instanceof Player) {
                Player s = (Player) sender;
                //Player only commands:
                if(cmd.getName().equalsIgnoreCase("hub")){
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + s.getName() + " Hub");
                    change.add(s);
                    s.getInventory().setItem(2, helpMenu());
                    s.getInventory().setItem(4, serverSelector());
                    s.getInventory().setItem(6, serverInfo());
                    s.getInventory().setHeldItemSlot(4);
                    change.remove(s);
                }

                if(cmd.getName().equalsIgnoreCase("help")){
                    settings.openMenu(s);
                }

                if (cmd.getName().equalsIgnoreCase("nick")) {
                    if (checkPlayer(args, 0, 2, sender)) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        getConfig().getConfigurationSection("nicknames").createSection(p.getName());
                        getConfig().getConfigurationSection("nicknames").set(p.getName(), args[1]);
                        p.setDisplayName(args[0]);
                        p.sendMessage(ChatColor.GREEN + "Set" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.GREEN + "'s nick to" + ChatColor.YELLOW + " " + ChatColor.BOLD + args[1]);
                    } else {
                        if (args.length == 1) {
                            Player p = (Player) sender;
                            getConfig().getConfigurationSection("nicknames").createSection(p.getName());
                            getConfig().getConfigurationSection("nicknames").set(p.getName(), args[0]);
                            p.setDisplayName(args[0]);
                            p.sendMessage(ChatColor.GREEN + "Set" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + "'s " + ChatColor.GREEN + "nick to" + ChatColor.YELLOW + " " + ChatColor.BOLD + args[0]);
                        } else {
                            s.sendMessage(ChatColor.RED + cmd.getUsage());
                        }
                    }
                    return true;
                }

                if (cmd.getName().equalsIgnoreCase("chat")) {
                    if (mode.get(s) == "server") {
                        mode.remove(s);
                        mode.put(s, "global");
                        s.sendMessage(ChatColor.GREEN + "You are now chatting in " + ChatColor.YELLOW + ChatColor.BOLD + "Global" + ChatColor.GREEN + " chat.");
                    } else if(mode.get(s) == "global") {
                        mode.remove(s);
                        mode.put(s, "server");
                        s.sendMessage(ChatColor.GREEN + "You are now chatting in " + ChatColor.YELLOW + ChatColor.BOLD + "Server" + ChatColor.GREEN + " chat.");
                    } else {
                        s.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
                    }
                }
            } else {
                //Console only commands:
                if (cmd.getName().equalsIgnoreCase("setprisonrank")) {
                    if (checkPlayer(args, 0, 1, sender)) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        rank.remove(p);
                        rank.put(p, args[1]);
                        p.sendMessage(rank.get(p));
                        getConfig().getConfigurationSection("ranks").createSection(p.getName());
                        getConfig().getConfigurationSection("ranks").set(p.getName(), rank.get(p));
                        saveConfig();
                    }
                }
            }
            return true;
        } catch(Exception e){
            Bukkit.getServer().getLogger().severe("");
            Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING COMMAND: /" + cmd.getName() + " BY PLAYER " + sender.getName() + ":");
            Bukkit.getServer().getLogger().severe("");
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred while performing this command. Please contact Koenn.");
        }
        return true;
    }

    //Methods:
    private ItemStack serverSelector() {
        ItemStack i = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Server Selector");
        List<String> l = new ArrayList<String>();
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
        List<String> l = new ArrayList<String>();
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
        List<String> l = new ArrayList<String>();
        l.add(ChatColor.GREEN + "Click see server information");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    private void ShowServerInfo(Player p){
        serverInfo.forEach(p::sendMessage);
    }

    private boolean checkPlayer(String[] p, int i, int a, CommandSender s) {
        if(p.length < a){
            return false;
        }
        try{
            if (!(Bukkit.getServer().getPlayer(p[i]).isOnline())){
                return false;
            }
        } catch(NullPointerException e){
            return false;
        }
        return true;
    }

    private void sendMessage(Player o, Player p, String m, Boolean b, Boolean irc){
        if(!(p.getName().contains("Koenn")) && !(p.getName().contains("fredsandford007"))){
            m = m.toLowerCase();
        }
        PermissionUser user = PermissionsEx.getUser(p);
        String prefix = translateAlternateColorCodes('&', user.getPrefix());
        String r = getConfig().getConfigurationSection("ranks").getString(p.getName());
        String n;
        if(getConfig().getConfigurationSection("nicknames").contains(p.getName())) {
            n = getConfig().getConfigurationSection("nicknames").getString(p.getName());
        } else {
            n = p.getName();
        }
        if (irc) {
            o.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "[IRC] " + p.getName() + " " + ChatColor.WHITE + m);
        } else {
            if (b) {
                o.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.YELLOW + p.getWorld().getName() + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.GREEN + r + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
            } else {
                o.sendMessage(prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.GREEN + r + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
            }
        }
    }

    //Event Handlers:
    @EventHandler
    public void onAnyMove(PlayerMoveEvent e){
        try {
            Player p = e.getPlayer();
            try {
                realm.remove(p);
                realm.put(p, p.getWorld().getName());
            } catch (NullPointerException ex){
                p.sendMessage(ChatColor.RED + "An error occurred while locating your realm. Please contact Koenn.");
                Bukkit.getServer().getLogger().severe("PLEASE SPECIFY A DEFAULT REALM");
            }

            if(p.getWorld().getName().contains("Hub")){
                if(p.getGameMode() != GameMode.ADVENTURE)p.setGameMode(GameMode.ADVENTURE);
            }
        } catch(Exception ex){
            catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    @EventHandler
    public void onGamemodeToggle(PlayerGameModeChangeEvent e){
        Player p = e.getPlayer();
        try{
            if(realm.get(p).contains("Hub")){
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You are not allowed to change your gamemode here!");
                p.setGameMode(GameMode.ADVENTURE);
            }
        } catch(Exception ex){
            catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent e){
        try {
            e.setCancelled(true);
            Player p = e.getPlayer();
            if (e.getMessage().contains("connected with an Android device using MineChat")) {
                if (p.getName().equals("Koenn")){
                    e.setMessage("[IRC] Koenn connected with IRC");
                    irc.put(p, true);
                } else {
                    p.kickPlayer(ChatColor.DARK_RED + "MineChat is not allowed on this server!");
                }
            } else {
                if (mute.containsKey(p)) return;
                if (e.getMessage().equalsIgnoreCase(spam.get(p))) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Spamming is not allowed!");
                } else {
                    if (nick.get(p) == null) {
                        nick.put(p, p.getName());
                    }
                    if (realm.get(p) == null) {
                        realm.put(p, p.getWorld().getName());
                    }
                    if (mode.get(p) == null) {
                        mode.put(p, "server");
                    }
                    for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                        if(irc.get(p)){
                            sendMessage(o, p, e.getMessage(), true, true);
                        } else {
                            if (mode.get(o) == "global") {
                                sendMessage(o, p, e.getMessage(), true, false);
                            } else {
                                if (realm.get(o) == realm.get(p)) {
                                    sendMessage(o, p, e.getMessage(), false, false);
                                }
                            }
                        }
                    }
                    spam.remove(p);
                    spam.put(p, e.getMessage());
                }
            }
        } catch(Exception ex){
            catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        getConfig().getConfigurationSection("ranks").createSection(p.getName());
        getConfig().getConfigurationSection("ranks").set(p.getName(), "A");
        mode.remove(p);
        mode.put(p, "server");
        spam.remove(p);
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if(p.getWorld().getName().contains("Hub")){
            p.getInventory().setItem(2, helpMenu());
            p.getInventory().setItem(4, serverSelector());
            p.getInventory().setItem(6, serverInfo());
            if(!(change.contains(p))){
                change.add(p);
                if (e.getNewSlot() > e.getPreviousSlot()){
                    if(e.getPreviousSlot() == 6){
                        p.getInventory().setHeldItemSlot(2);
                    }
                    if(e.getPreviousSlot() == 4){
                        p.getInventory().setHeldItemSlot(6);
                    }
                    if(e.getPreviousSlot() == 2){
                        p.getInventory().setHeldItemSlot(4);
                    }
                } else {
                    if(e.getPreviousSlot() == 6){
                        p.getInventory().setHeldItemSlot(4);
                    }
                    if(e.getPreviousSlot() == 4){
                        p.getInventory().setHeldItemSlot(2);
                    }
                    if(e.getPreviousSlot() == 2){
                        p.getInventory().setHeldItemSlot(6);
                    }
                }
                change.remove(p);
            }
        }
    }

    @EventHandler
    public void onRightClick1(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInHand().equals(serverSelector())) {
            e.setCancelled(true);
            ivm.openServerSelector(p);
        }
        if(p.getInventory().getItemInHand().equals(helpMenu())) {
            e.setCancelled(true);
            p.performCommand("help");
        }
        if(p.getInventory().getItemInHand().equals(serverInfo())) {
            e.setCancelled(true);
            ShowServerInfo(p);
        }
    }

    @EventHandler
    public void onRightClick2(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInHand().equals(serverSelector())) {
            e.setCancelled(true);
            ivm.openServerSelector(p);
        }
        if(p.getInventory().getItemInHand().equals(helpMenu())) {
            e.setCancelled(true);
            p.performCommand("help");
        }
        if(p.getInventory().getItemInHand().equals(serverInfo())) {
            e.setCancelled(true);
            ShowServerInfo(p);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(p.getWorld().getName().contains("Hub")){
            e.setCancelled(true);
            ivm.openServerSelector(p);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        try {
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem().equals(serverSelector())){
                ivm.openServerSelector(p);
                e.setCancelled(true);
            }
            if (e.getCursor().equals(serverSelector())){
                ivm.openServerSelector(p);
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
                ShowServerInfo(p);
            }
            if(e.getCursor().equals(serverInfo())) {
                e.setCancelled(true);
                ShowServerInfo(p);
            }
            if (e.getInventory().getName().contains(ivm.getServerSelector())){
                e.setCancelled(true);
                switch(e.getSlot()){
                    case 0:
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Hub");
                        break;
                    case 1:
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Prison");
                        break;
                    case 2:
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Minigames");
                        break;
                    case 3:
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Skyblock");
                        break;
                    case 4:
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gotoserver " + p.getName() + " Factions");
                        break;
                }
            }
        } catch(Exception ex){
            catchEvent(ex, (Player) e.getWhoClicked(), e.getEventName());
        }
    }

    public void catchEvent(Exception e, Player p, String s){
        Bukkit.getServer().getLogger().severe("");
        Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING " + s + " BY PLAYER " + p.getName() + ":");
        Bukkit.getServer().getLogger().severe("");
        e.printStackTrace();
        p.sendMessage(ChatColor.RED + "An error occurred while doing this. Please contact Koenn.");
    }
}