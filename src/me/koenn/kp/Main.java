package me.koenn.kp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
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

    public String[] realms;

    public InventoryManager ivm;
    public SettingsMenu settings;

    //OnEnable:
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        this.ivm = new InventoryManager();
        this.settings = new SettingsMenu(this);

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
            realms[i] = entry.getKey().toString();
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
                if (checkPlayer(args, 0, 1, sender)) {
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    p.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + cmd.getUsage());
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("gotoserver")){
                if (checkPlayer(args, 0, 2, sender)){
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + p.getName() + " " + args[1]);
                    ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "Welcome to the " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Hexic " + args[1] + ChatColor.GREEN + " Server!");
                    Bukkit.getLogger().severe(getConfig().get("defaultRealm").toString() + " -- " + args[1]);
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
        } catch(NullPointerException e){
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

    private void sendMessage(Player o, Player p, String m, Boolean b) {
        PermissionUser user = PermissionsEx.getUser(p);
        String prefix = translateAlternateColorCodes('&', user.getPrefix());
        String r = getConfig().getConfigurationSection("ranks").getString(p.getName());
        String n;
        if(getConfig().getConfigurationSection("nicknames").contains(p.getName())) {
            n = getConfig().getConfigurationSection("nicknames").getString(p.getName());
        } else {
            n = p.getName();
        }
        if (b) {
            o.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.YELLOW + realm.get(p) + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.GREEN + r + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE  + m);
        } else {
            o.sendMessage(prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.GREEN + r + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
        }
    }

    //Event Handlers:
    @EventHandler
    public void onAnyMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        try{
            realm.remove(p);
            realm.put(p, getConfig().getConfigurationSection("realms").get(p.getWorld().getName()).toString());
        } catch(NullPointerException ex){
            realm.remove(p);
            try{
                realm.put(p, getConfig().get("defaultRealm").toString());
            } catch(NullPointerException exc){
                p.sendMessage(ChatColor.RED + "An error occurred while locating your realm. Please contact Koenn.");
                Bukkit.getServer().getLogger().severe("PLEASE SPECIFY A DEFAULT REALM");
            }
        }

        if(p.getWorld().toString() == "Hub"){
            if(p.getGameMode() != GameMode.ADVENTURE)p.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent e){
        e.setCancelled(true);
        Player p = e.getPlayer();
        if(e.getMessage().contains("connected with an Android device using MineChat")){
            if(p.toString() == "Koenn"){
                e.setMessage("[IRC] Koenn connected with IRC");
                irc.put(p, true);
            } else {
                p.kickPlayer(ChatColor.DARK_RED + "MineChat is not allowed on this server!");
            }
        } else {
            if (mute.containsKey(p))return;
            if (e.getMessage().equalsIgnoreCase(spam.get(p))){
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Spamming is not allowed!");
                return;
            } else {
                if(nick.get(p) == null){
                    nick.put(p, p.getName());
                }
                if(realm.get(p) == null){
                    realm.put(p, p.getWorld().getName());
                }
                if(mode.get(p) == null){
                    mode.put(p, "server");
                }
                for(Player o: Bukkit.getServer().getOnlinePlayers()){
                    if(mode.get(o) == "global"){
                        sendMessage(o, p, e.getMessage(), true);
                    } else {
                        if(realm.get(o) == realm.get(p)){
                            sendMessage(o, p, e.getMessage(), false);
                        }
                    }
                }

                spam.remove(p);
                spam.put(p, e.getMessage());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        mode.remove(p);
        mode.put(p, "server");
        spam.remove(p);
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if(realm.get(p) == getConfig().get("defaultRealm")){
            p.getInventory().setHeldItemSlot(4);
            p.getInventory().setItemInHand(serverSelector());
        }
    }

    @EventHandler
    public void onRightClick1(PlayerInteractEvent e){
        ivm.openServerSelector(e.getPlayer());
    }

    @EventHandler
    public void onRightClick2(PlayerInteractEntityEvent e){
        ivm.openServerSelector(e.getPlayer());
    }

    @EventHandler
    public void onRightClick(PlayerDropItemEvent e){
        ivm.openServerSelector(e.getPlayer());
    }

    @EventHandler
    public void onRightClick(InventoryClickEvent e){
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if(new ItemStack(e.getCurrentItem().getType(), 1) == serverSelector()){
            ivm.openServerSelector(p);
        }
        if(new ItemStack(e.getCursor().getType(), 1) == serverSelector()){
            ivm.openServerSelector(p);
        }
    }
}