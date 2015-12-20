package me.koenn.kp.listeners;

import me.koenn.kp.ConfigManager;
import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChange implements Listener {

    private Main main;
    private ConfigManager cm;

    public OnSignChange(Main main) {
        this.main = main;
        cm = ConfigManager.getInstance();
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        try {
            if(e.getLine(0).contains("[Cell]")){
                if(e.getPlayer().isOp()){
                    Player p = e.getPlayer();
                    if(cm.getInt("ncl") != null){
                        cm.set("ncl", (cm.getInt("ncl") + 1));
                    } else {
                        cm.set("ncl", 1);
                    }
                    int ncl = cm.getInt("ncl");
                    e.setLine(0, ChatColor.DARK_BLUE + "[Cell]");
                    e.setLine(1, "Cell" + ncl);
                    e.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Empty");
                    e.setLine(3, ChatColor.GRAY + "" + ChatColor.BOLD + "Click to rent!");
                    main.getConfig().createSection("cell" + ncl);
                    ConfigurationSection ws = main.getConfig().getConfigurationSection("cell" + ncl);
                    main.log(ws + " -- " + e.getBlock().getWorld().getName());
                    ws.set("world", e.getBlock().getWorld().getName());
                    ws.set("x", e.getBlock().getLocation().getX());
                    ws.set("y", e.getBlock().getLocation().getY());
                    ws.set("z", e.getBlock().getLocation().getZ());
                    ws.set("setup", false);
                    ws.set("owner", "null");
                    main.saveConfig();
                    main.log(p.getName() + " created cell " + ncl);
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Created Cell" + ncl);
                } else {
                    main.noPerms(e.getPlayer());
                }
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}