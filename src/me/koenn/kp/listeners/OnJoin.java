package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener{

    private Main main;

    public OnJoin(Main main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        try{
            Player p = e.getPlayer();
            p.setOp(false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> p.performCommand("ircusers"), 20);
            main.mode.remove(p);
            main.mode.put(p, "server");
            main.spam.remove(p);
        }catch (Exception ex){
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}
