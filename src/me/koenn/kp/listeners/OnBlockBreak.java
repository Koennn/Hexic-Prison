package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnBlockBreak implements Listener {

    private Main main;

    public OnBlockBreak(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        try {
            Player p = e.getPlayer();
            if(p.getInventory().firstEmpty() == -1){
                p.performCommand("sellall");
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}