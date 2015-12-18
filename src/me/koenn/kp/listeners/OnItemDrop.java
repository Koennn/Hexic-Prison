package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnItemDrop implements Listener {

    private Main main;

    public OnItemDrop(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        try {
            Player p = e.getPlayer();
            if(p.getWorld().getName().contains("Hub")){
                e.setCancelled(true);
                main.ivm.openServerSelector(p);
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}