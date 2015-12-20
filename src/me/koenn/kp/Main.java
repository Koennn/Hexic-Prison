package me.koenn.kp;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.koenn.kp.commands.CommandHandler;
import me.koenn.kp.commands.MessageManager;
import me.koenn.kp.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin{

    public InventoryManager ivm;
    public SettingsMenu settings;
    public Warping warping;

    public HashMap<Player, Boolean> mute = new HashMap<>();
    public HashMap<Player, String> spam = new HashMap<>();
    public HashMap<Player, String> mode = new HashMap<>();
    public HashMap<UUID, String> admins = new HashMap<>();
    public ArrayList<Player> block = new ArrayList<>();

    public String textColor = ChatColor.GREEN + "";
    public String titleColor = ChatColor.YELLOW + "" + ChatColor.BOLD;
    public String hexicTitle = ChatColor.GOLD + "" + ChatColor.BOLD;

    public File saveTo = new File(getDataFolder(), "CommandLog.txt");

    public static WorldGuardPlugin wg = null;

    public void log(String msg, String player){
        try{
            Date now = new Date();
            SimpleDateFormat stamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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

    public void disablePlugin(String reason){
        this.setEnabled(false);
        log("Disabled due to " + reason);
    }

    public void onEnable() {
        this.wg = getWorldGuard();

        Money money = new Money(this);
        CommandHandler ch = new CommandHandler();
        Ranks ranks = new Ranks(this);
        this.warping = new Warping(this);
        this.ivm = new InventoryManager(this, this);
        this.settings = new SettingsMenu(this);

        ConfigManager.getInstance().setup(this, this);

        ch.setup(this, ranks, ivm, warping);
        getCommand("warn").setExecutor(ch);
        getCommand("adminmode").setExecutor(ch);
        getCommand("adminregister").setExecutor(ch);
        getCommand("chat").setExecutor(ch);
        getCommand("gotoserver").setExecutor(ch);
        getCommand("history").setExecutor(ch);
        getCommand("hub").setExecutor(ch);
        getCommand("mute").setExecutor(ch);
        getCommand("nick").setExecutor(ch);
        getCommand("relog").setExecutor(ch);
        getCommand("help").setExecutor(ch);
        getCommand("rankup").setExecutor(ch);
        getCommand("setrank").setExecutor(ch);
        getCommand("emoney").setExecutor(ch);
        getCommand("bal").setExecutor(ch);
        getCommand("pay").setExecutor(ch);
        getCommand("warp").setExecutor(ch);
        getCommand("setwarp").setExecutor(ch);
        getCommand("unrent").setExecutor(ch);

        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(settings, this);
        pm.registerEvents(new Lapis(), this);
        pm.registerEvents(new OnJoin(this, ranks), this);
        pm.registerEvents(new OnFlight(this), this);
        pm.registerEvents(new OnBlockBreak(this), this);
        pm.registerEvents(new OnDeath(this), this);
        pm.registerEvents(new OnAnyMove(this, this), this);
        pm.registerEvents(new OnChat(this, ranks), this);
        pm.registerEvents(new CommandPreprocess(this), this);
        pm.registerEvents(new OnRightclick(this, money), this);
        pm.registerEvents(new OnInventoryClick(this, warping), this);
        pm.registerEvents(new OnItemDrop(this), this);
        pm.registerEvents(new OnLeave(this), this);
        pm.registerEvents(new OnSignChange(this), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.broadcastMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "If you find any bugs, use /report to report a bug."), 1, 8400);
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Broadcast] " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Loading additional resources...");
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin w = getServer().getPluginManager().getPlugin("WorldGuard");
        if ((w == null) || (!(w instanceof WorldGuardPlugin))) {
            disablePlugin("Failed to hook into WorldGuard");
            return null;
        }
        log("Successfully hooked into WorldGuard");
        return (WorldGuardPlugin)w;
    }

    public void log(String s){
        Bukkit.getLogger().log(Level.INFO, "[HexicPrison] " + s);
    }

    public void ShowServerInfo(Player p){
        p.performCommand("info");
    }

    @SuppressWarnings("deprecation")
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

    public void catchEvent(Exception e, Player p, String s){
        Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING '" + s + "' BY PLAYER '" + p.getName() + "':");
        e.printStackTrace();
        MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "An error occurred while doing this. Please contact Koenn.");
    }

    public void noPerms(Player p){
        MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "You do not have the permission to do this.");
    }
}