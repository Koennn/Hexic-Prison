package me.koenn.kp.listeners;

import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnAnyMove implements Listener {

    private Main main;

    public OnAnyMove(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onAnyMove(PlayerMoveEvent e) {
        try {
            Player p = e.getPlayer();
            if(p.getWorld().getName().contains("Hub")){
                if(p.getGameMode() != GameMode.ADVENTURE && !(main.admins.containsKey(p.getUniqueId()))){
                    p.setGameMode(GameMode.ADVENTURE);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 4, true, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 2, true, false));
                }
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}