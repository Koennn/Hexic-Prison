package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeath implements Listener {

    private Main main;

    public OnDeath(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        try {
            MessageManager.getInstance().msg(e.getEntity().getKiller(), MessageManager.MessageType.WARN, "You earned $500 by killing " + e.getEntity());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "emoney add 500 " + e.getEntity().getKiller().getName());
        } catch (Exception ex) {
            main.catchEvent(ex, e.getEntity(), e.getEventName());
        }
    }

}