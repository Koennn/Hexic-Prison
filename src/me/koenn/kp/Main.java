package me.koenn.kp;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.koenn.kp.commands.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Main extends JavaPlugin implements Listener {

    //Variables:
    HashMap<Player, Boolean> irc = new HashMap<>();
    public HashMap<Player, Boolean> mute = new HashMap<>();
    HashMap<Player, String> spam = new HashMap<>();
    HashMap<Player, String> realm = new HashMap<>();
    public HashMap<Player, String> rank = new HashMap<>();
    HashMap<Player, String> nick = new HashMap<>();
    public HashMap<Player, String> mode = new HashMap<>();
    ArrayList<Player> change = new ArrayList<>();
    public HashMap<UUID, String> admins = new HashMap<>();
    public ArrayList<Player> block = new ArrayList<>();

    public HashMap<Player, Integer> warns = new HashMap<>();
    public HashMap<Player, Integer> mutes = new HashMap<>();

    public String[] realms;

    public File saveTo = new File(getDataFolder(), "CommandLog.txt");

    Date now = new Date();
    SimpleDateFormat stamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public InventoryManager ivm;
    public SettingsMenu settings;
    private CommandHandler ch;

    public String textColor = ChatColor.GREEN + "";
    public String titleColor = ChatColor.YELLOW + "" + ChatColor.BOLD;
    public String hexicTitle = ChatColor.GOLD + "" + ChatColor.BOLD;

    public void log(String msg, String player){
        try{
            if(!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("[" + stamp.format(now) + "] (" + player + ") " + msg);
            pw.flush();
            pw.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //OnEnable:
    public void onEnable() {
        this.ch = new CommandHandler();

        ch.setup(this);
        getCommand("warn").setExecutor(ch);
        getCommand("adminmode").setExecutor(ch);
        getCommand("adminregister").setExecutor(ch);
        getCommand("chat").setExecutor(ch);
        getCommand("gotoserver").setExecutor(ch);
        getCommand("history").setExecutor(ch);
        getCommand("hub").setExecutor(ch);
        getCommand("mute").setExecutor(ch);


        Bukkit.getServer().getPluginManager().registerEvents(new Lapis(), this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.broadcastMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "If you find any bugs, use /report to report a bug."), 1, 8400);

        Bukkit.broadcastMessage(ChatColor.BLUE + "[Broadcast] " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Loading additional resources...");

        this.ivm = new InventoryManager(this, this);
        this.settings = new SettingsMenu(this);

        Bukkit.getServer().getPluginManager().registerEvents(settings, this);

        if(!(getDataFolder().exists())) {
            getConfig().options().copyDefaults(true);
            saveConfig();
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

    public void addWarning(Player p){
        ConfigurationSection w = getConfig().getConfigurationSection("warns");
        Integer a;
        try{
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex){
            w.createSection(p.getUniqueId().toString());
            a = 0;
        }
        w.set(p.getUniqueId().toString(), (a + 1));
        saveConfig();
    }

    public void addMute(Player p){
        ConfigurationSection w = getConfig().getConfigurationSection("mutes");
        Integer a;
        try{
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex){
            w.createSection(p.getUniqueId().toString());
            a = 0;
        }
        w.set(p.getUniqueId().toString(), (a + 1));
        saveConfig();
    }

    public Integer getWarns(Player p){
        ConfigurationSection w = getConfig().getConfigurationSection("warns");
        Integer a;
        try{
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex){
            w.createSection(p.getUniqueId().toString());
            a = 0;
            Bukkit.getLogger().log(Level.INFO, "Failed to load warns for player... Setting value to 0.");
        }
        return a;
    }

    public Integer getMutes(Player p){
        ConfigurationSection w = getConfig().getConfigurationSection("mutes");
        Integer a;
        try{
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex){
            w.createSection(p.getUniqueId().toString());
            a = 0;
            log("Failed to load warns for player... Setting value to 0.");
        }
        return a;
    }

    public void log(String s){
        Bukkit.getLogger().log(Level.INFO, "[HexicPrison] " + s);
    }

    //Methods:
    public ItemStack serverSelector() {
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

    public ItemStack helpMenu(){
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

    public ItemStack serverInfo(){
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
        p.performCommand("info");
    }

    public boolean checkPlayer(String[] p, int i, int a, CommandSender s) {
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

    private void sendMessage(Player o, Player p, String m, Boolean b, Boolean irc, String mode){
        if(!(p.getName().contains("Koenn")) && !(p.getName().contains("fredsandford007")) && !(p.getName().contains("Shenz"))){
            m = m.toLowerCase();
        }
        PermissionUser user = PermissionsEx.getUser(p);
        String prefix = translateAlternateColorCodes('&', user.getPrefix());
        if(user.getPrefix() == "") {
            prefix = translateAlternateColorCodes('&', "&8&l(&fDefault&8&l) ");
        }
        String r = rank.get(p);
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
                o.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.YELLOW + p.getWorld().getName() + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
            } else {
                if(mode == "Prison"){
                    o.sendMessage(prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "(" + ChatColor.GREEN + r + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ") " + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
                } else {
                    o.sendMessage(prefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "" + n + ": " + ChatColor.WHITE + m);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        try{
            Player p = e.getPlayer();
            if(p.getInventory().firstEmpty() == -1){
                p.performCommand("sellall");
            }
        }catch (Exception ex){
            catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e){
        try{
            e.getEntity().getKiller().sendMessage(textColor + "You earned $500 by killing " + e.getEntity());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "emoney add 500 " + e.getEntity().getKiller().getName());
        }catch (Exception ex){
            catchEvent(ex, e.getEntity(), e.getEventName());
        }
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent e){
        Player p = (Player) e.getPlayer();
        if(!(p.hasPermission("essentials.fly"))){
            e.setCancelled(true);
            noPerms(p);
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
                Bukkit.getLogger().severe("PLEASE SPECIFY A DEFAULT REALM");
            }

            if(p.getWorld().getName().contains("Hub")){
                if(p.getGameMode() != GameMode.ADVENTURE && !(admins.containsKey(p.getUniqueId()))){
                    p.setGameMode(GameMode.ADVENTURE);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 4, true, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 2, true, false));
                }
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
                    Bukkit.broadcastMessage("[IRC] Koenn connected with IRC");
                    p.setGameMode(GameMode.SPECTATOR);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "nte prefix " + p.getName() + " &f[IRC] &f");
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
                        if(irc.containsKey(p)){
                            sendMessage(o, p, e.getMessage(), true, true, "Normal");
                        } else {
                            if (mode.get(o) == "global") {
                                sendMessage(o, p, e.getMessage(), true, false, "Normal");
                            } else {
                                if (o.getWorld().getName() == p.getWorld().getName()) {
                                    if(p.getWorld().getName().contains("Prison")){
                                        sendMessage(o, p, e.getMessage(), false, false, "Prison");
                                    } else {
                                        sendMessage(o, p, e.getMessage(), false, false, "Normal");
                                    }
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
    public void onLeave(PlayerQuitEvent e){
        try{
            Player p = e.getPlayer();
            p.setOp(false);
            if(irc.containsKey(p)){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "nte prefix " + p.getName() + "&9");
                p.setGameMode(GameMode.ADVENTURE);
            }
        } catch(Exception ex){
            catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setOp(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> p.performCommand("ircusers"), 20);
        mode.remove(p);
        mode.put(p, "server");
        spam.remove(p);
        if(p.getName().contains("Koenn")){
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "nte prefix " + p.getName() + " &9"), 10);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "nte prefix " + p.getName() + "&9");
            p.setGameMode(GameMode.ADVENTURE);
            irc.remove(p);
        }
    }

    //@EventHandler
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
        try {
            if (p.getInventory().getItemInHand().equals(serverSelector())) {
                e.setCancelled(true);
                ivm.openServerSelector(p);
            }
            if (p.getInventory().getItemInHand().equals(helpMenu())) {
                e.setCancelled(true);
                p.performCommand("help");
            }
            if (p.getInventory().getItemInHand().equals(serverInfo())) {
                e.setCancelled(true);
                ShowServerInfo(p);
            }
        } catch(Exception ex){
            //TODO: Fix error
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
            if(!(e.getCurrentItem() == (null) && !(e.getCursor() == (null)))){
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
                if(e.getInventory().getName().contains("Information about ")){
                    e.setCancelled(true);
                    if(e.getSlot() == 4){
                        p.teleport(ivm.history.get(p));
                        p.sendMessage(textColor + "Teleporting to " + titleColor + ivm.history.get(p).getName());
                    }
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
        } catch(Exception ex){
            catchEvent(ex, (Player) e.getWhoClicked(), e.getEventName());
        }
    }

    @EventHandler
    public void commandHandler(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(e.getMessage().contains(":") && !(e.getMessage().contains("warp")) && !(e.getMessage().contains("kit")) && !(p.isOp())){
            e.setCancelled(true);
            p.sendMessage(hexicTitle + "[Hexic] " + textColor + "This command is not allowed!");
        }
        if(block.contains(p)){
            if(!(e.getMessage().contains("leave")) && !(e.getMessage().contains("kit"))){
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You cant do that in a PvP arena!");
            }
        }
        if(p.getWorld().getName().contains("Hub")){
            if(!(p.isOp())) {
                if (!(e.getMessage().contains("help")) && !(e.getMessage().contains("adminmode")) && !(e.getMessage().contains("hub"))){
                    e.setCancelled(true);
                    p.sendMessage(hexicTitle + "[Hexic] " + textColor + "We dont know this command, use " + titleColor + "/help " + textColor + "for a list of commands.");
                }
            }
        }
        if(p.isOp()){
            log(e.getMessage(), p.getName());
        }
    }

    public void catchEvent(Exception e, Player p, String s){
        Bukkit.getServer().getLogger().severe("");
        Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING '" + s + "' BY PLAYER '" + p.getName() + "':");
        Bukkit.getServer().getLogger().severe("");
        e.printStackTrace();
        p.sendMessage(ChatColor.RED + "An error occurred while doing this. Please contact Koenn.");
    }

    public void noPerm(CommandSender p, Command cmd){
        p.sendMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "You do not have the permission to do this.");
        Bukkit.getLogger().log(Level.INFO, "Player " + p.getName() + " tried to execute command " + cmd.getName());
    }

    public void noPerms(Player p){
        p.sendMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "You do not have the permission to do this.");
    }

    @EventHandler
    public void onVote(VotifierEvent e){
        if(!(Bukkit.getServer().getPlayer(e.getVote().getUsername()).isOnline())){
            Player p = Bukkit.getServer().getPlayer(e.getVote().getUsername());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "key " + p.getName() + " voting");
        }
    }

}