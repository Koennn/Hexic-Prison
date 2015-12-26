package me.koenn.kp.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.koenn.kp.Main;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

public class OnAnyMove implements Listener {

    private static Integer time;
    private Main main;
    private Plugin plugin;
    private Integer taskID1;
    private Integer taskID2;


    public OnAnyMove(Main main, Plugin plugin) {
        this.main = main;
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnyMove(PlayerMoveEvent e) {
        try {
            Player p = e.getPlayer();
            if(p.getWorld().getName().contains("Hub")){
                if(p.getGameMode() != GameMode.ADVENTURE && !(main.admins.containsKey(p.getUniqueId()))){
                    p.setGameMode(GameMode.ADVENTURE);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 4, true, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 2, true, false));
            }
            Location loc = p.getLocation();
            Vector v = new Vector(loc.getX(), loc.getBlockY(), loc.getZ());
            ApplicableRegionSet set = Main.wg.getRegionManager(loc.getWorld()).getApplicableRegions(v);
            Iterator<ProtectedRegion> iter = set.iterator();
            ProtectedRegion region = null;
            while(iter.hasNext()) {
                ProtectedRegion nextRegion = iter.next();
                if(region == null || region.getPriority() > region.getPriority()) region = nextRegion;
            }
            try{
                region.getId();
            } catch (Exception ex){
                return;
            }
            if(region.getId().contains("cell")) {
                ConfigurationSection s = main.getConfig().getConfigurationSection(region.getId());
                if (!region.getMembers().contains(p.getUniqueId())) {
                    World world = Bukkit.getWorld(s.get("tworld").toString());
                    Double x = s.getDouble("tx");
                    Double y = s.getDouble("ty");
                    Double z = s.getDouble("tz");
                    float yaw = loc.getYaw();
                    float pitch = loc.getPitch();
                    p.teleport(new Location(world, x, y, z, yaw, pitch));
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "This is not your cell!");
                }
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}