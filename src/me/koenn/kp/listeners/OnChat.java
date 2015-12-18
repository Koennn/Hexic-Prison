package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class OnChat implements Listener {

    private Main main;

    public OnChat(Main main) {
        this.main = main;
    }

    public void sendMessage(Player o, Player p, String m, Boolean b, String mode){
        if(!(p.getName().contains("Koenn")) && !(p.getName().contains("fredsandford007")) && !(p.getName().contains("Shenz"))){
            m = m.toLowerCase();
        }
        PermissionUser user = PermissionsEx.getUser(p);
        String prefix = translateAlternateColorCodes('&', user.getPrefix());
        if(user.getPrefix() == "") {
            prefix = translateAlternateColorCodes('&', "&8&l(&fDefault&8&l) ");
        }
        String r = main.rank.get(p);
        String n;
        if(main.getConfig().getConfigurationSection("nicknames").contains(p.getName())) {
            n = main.getConfig().getConfigurationSection("nicknames").getString(p.getName());
        } else {
            n = p.getName();
        }
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

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onChat(PlayerChatEvent e) {
        try {
            e.setCancelled(true);
            Player p = e.getPlayer();
            if (main.mute.containsKey(p)) return;
            if (e.getMessage().equalsIgnoreCase(main.spam.get(p))) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Spamming is not allowed!");
            } else {
                if (main.nick.get(p) == null) {
                    main.nick.put(p, p.getName());
                }
                if (main.mode.get(p) == null) {
                    main.mode.put(p, "server");
                }
                for (Player o : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.mode.get(o) == "global") {
                        sendMessage(o, p, e.getMessage(), true, "Normal");
                    } else {
                        if (o.getWorld().getName() == p.getWorld().getName()) {
                            if(p.getWorld().getName().contains("Prison")){
                                sendMessage(o, p, e.getMessage(), false, "Prison");
                            } else {
                                sendMessage(o, p, e.getMessage(), false, "Normal");
                            }
                        }
                    }
                }
                main.spam.remove(p);
                main.spam.put(p, e.getMessage());
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}