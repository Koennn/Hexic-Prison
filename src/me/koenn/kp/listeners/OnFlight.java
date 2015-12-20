package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class OnFlight implements Listener {

    private Main main;

    public OnFlight(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent e) {
        try {
            Player p = e.getPlayer();
            if(!(p.hasPermission("essentials.fly"))){
                e.setCancelled(true);
                main.noPerms(p);
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}