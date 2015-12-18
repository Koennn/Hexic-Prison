package me.koenn.kp;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.koenn.kp.commands.CommandHandler;
import me.koenn.kp.listeners.*;
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

    public InventoryManager ivm;
    public SettingsMenu settings;

    public HashMap<Player, Boolean> mute = new HashMap<>();
    public HashMap<Player, String> spam = new HashMap<>();
    public HashMap<Player, String> rank = new HashMap<>();
    public HashMap<Player, String> nick = new HashMap<>();
    public HashMap<Player, String> mode = new HashMap<>();
    public HashMap<UUID, String> admins = new HashMap<>();
    public ArrayList<Player> block = new ArrayList<>();

    public String textColor = ChatColor.GREEN + "";
    public String hexicTitle = ChatColor.GOLD + "" + ChatColor.BOLD;

    public File saveTo = new File(getDataFolder(), "CommandLog.txt");

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

    public void onEnable() {
        CommandHandler ch = new CommandHandler();
        this.ivm = new InventoryManager(this, this);
        this.settings = new SettingsMenu(this);

        ch.setup(this);
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
        getCommand("ranks").setExecutor(ch);

        Bukkit.getServer().getPluginManager().registerEvents(settings, this);
        Bukkit.getServer().getPluginManager().registerEvents(new Lapis(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnFlight(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnBlockBreak(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnDeath(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnAnyMove(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnChat(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CommandPreprocess(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnRightclick(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnInventoryClick(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnItemDrop(this), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.broadcastMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "If you find any bugs, use /report to report a bug."), 1, 8400);
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Broadcast] " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Loading additional resources...");

        if(!(getDataFolder().exists())) {
            getConfig().options().copyDefaults(true);
            saveConfig();
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
            log("Failed to load mutes for player... Setting value to 0.");
        }
        return a;
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
        Bukkit.getServer().getLogger().severe("");
        Bukkit.getServer().getLogger().severe("ERROR WHILE PERFORMING '" + s + "' BY PLAYER '" + p.getName() + "':");
        Bukkit.getServer().getLogger().severe("");
        e.printStackTrace();
        p.sendMessage(ChatColor.RED + "An error occurred while doing this. Please contact Koenn.");
    }

    public void noPerms(Player p){
        p.sendMessage(hexicTitle + hexicTitle + "[Hexic] " + textColor + "You do not have the permission to do this.");
    }
}