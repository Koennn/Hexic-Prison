package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class OnJoin implements Listener{

    private Main main;

    public OnJoin(Main main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        try{
            Player p = e.getPlayer();
            PermissionUser user = PermissionsEx.getUser(p);
            String prefix = translateAlternateColorCodes('&', user.getPrefix());
            if(user.getPrefix() == "") {
                prefix = " ";
            }
            String n;
            if(main.getConfig().getConfigurationSection("nicknames").contains(p.getName())) {
                n = main.getConfig().getConfigurationSection("nicknames").getString(p.getName());
            } else {
                n = p.getName();
            }
            if(!e.getPlayer().hasPlayedBefore()) {
                for(int i = 0; i < 94; i++){
                    p.sendMessage("");
                }
                sendJoin(p);
                Bukkit.broadcastMessage(translateAlternateColorCodes('&', "&7Welcome &e&l" + n + " &7to &6&lHexic Network!"));
                return;
            }
            e.setJoinMessage(translateAlternateColorCodes('&', "&7[&2&l+&7]" + prefix + "&8" + n));
            p.setOp(false);
            main.mode.remove(p);
            main.mode.put(p, "server");
            main.spam.remove(p);
            for(int i = 0; i < 94; i++){
                p.sendMessage("");
            }
            sendJoin(p);
            if(main.getConfig().getConfigurationSection("passwords").contains(p.getUniqueId().toString())){
                p.sendMessage(translateAlternateColorCodes('&', "&cAdminmode is disabled for '%player%'. Use /adminmode <password> to enable."));
            }
            if(p.isOp()){ p.performCommand("ircusers"); }
        }catch (Exception ex){
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

    private void sendJoin(Player p){
        p.sendMessage(translateAlternateColorCodes('&', "&f&l&m+---------------------------------------+"));
        p.sendMessage(translateAlternateColorCodes('&', "&r             &aWelcome &e&l" + p.getName() + " &ato &e&lHexic Network&a!"));
        p.sendMessage(translateAlternateColorCodes('&', "&r               &aNews: &b20% Christmas Release Sale"));
        p.sendMessage("");
        p.sendMessage(translateAlternateColorCodes('&', "&r                       &cStatus: &7Development"));
        p.sendMessage(translateAlternateColorCodes('&', "&eVote daily using &c/vote &eto get special rewards."));
        p.sendMessage(translateAlternateColorCodes('&', "&ePurchase commands and ranks using &d/buy &eor by the site."));
        p.sendMessage(translateAlternateColorCodes('&', "&f&l&m+---------------------------------------+"));
    }

}
