package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CommandPreprocess implements Listener {

    private Main main;

    public CommandPreprocess(Main main) {
        this.main = main;
    }

    @EventHandler
    public void commandPreprocess(PlayerCommandPreprocessEvent e) {
        try {
            Player p = e.getPlayer();
            if(e.getMessage().contains(":") && !(e.getMessage().contains("warp")) && !(e.getMessage().contains("kit")) && !(p.isOp())){
                e.setCancelled(true);
                MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "This command is not allowed!");
            }
            if(main.block.contains(p)){
                if(!(e.getMessage().contains("leave")) && !(e.getMessage().contains("kit"))){
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You cant do that in a PvP arena!");
                }
            }
            if(p.isOp()){
                main.log(e.getMessage(), p.getName());
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}