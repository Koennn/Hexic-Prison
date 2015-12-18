package me.koenn.kp.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnVote implements Listener {

    private Main main;

    public OnVote(Main main) {
        this.main = main;
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onVote(VotifierEvent e) {
        try {
            if(!(Bukkit.getServer().getPlayer(e.getVote().getUsername()).isOnline())){
                Player p = Bukkit.getServer().getPlayer(e.getVote().getUsername());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "key " + p.getName() + " voting");
            }
        } catch (Exception ex) {
            main.catchEvent(ex, Bukkit.getServer().getPlayer(e.getVote().getUsername()), e.getEventName());
        }
    }

}