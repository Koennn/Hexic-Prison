package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class OnLeave implements Listener {

    private Main main;

    public OnLeave(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        try {
            Player p = e.getPlayer();
            PermissionUser user = PermissionsEx.getUser(p);
            String prefix = translateAlternateColorCodes('&', user.getPrefix());
            if(user.getPrefix() == "") {
                prefix = "";
            }
            String n;
            if(main.getConfig().getConfigurationSection("nicknames").contains(p.getName())) {
                n = main.getConfig().getConfigurationSection("nicknames").getString(p.getName());
            } else {
                n = p.getName();
            }
            e.setQuitMessage(translateAlternateColorCodes('&', "&7[&4&l-&7]" + prefix + "&8 " + n));
            p.setOp(false);
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}