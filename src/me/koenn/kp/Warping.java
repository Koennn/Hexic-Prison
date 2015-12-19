package me.koenn.kp;

import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Warping {

    private Main main;

    public Warping(Main main){
        this.main = main;
    }

    public void click(Material warp, Player p){
        if(warp == Material.AIR)return;
        ConfigurationSection warps = main.getConfig().getConfigurationSection("warps");
        try{
            World world = Bukkit.getWorld(warps.getConfigurationSection(warp.toString()).get("world").toString());
            Double x = warps.getConfigurationSection(warp.toString()).getDouble("x");
            Double y = warps.getConfigurationSection(warp.toString()).getDouble("y");
            Double z = warps.getConfigurationSection(warp.toString()).getDouble("z");
            p.teleport(new Location(world, x, y, z));
            MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Warping to " + warps.getConfigurationSection(warp.toString()).get("name"));
        }catch (Exception e){
            MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "This warp does not exist!");
        }
    }

    public void setWarp(String name, String item, Player p){
        ConfigurationSection warps = main.getConfig().getConfigurationSection("warps");
        Material icon;
        try{
            icon = Material.valueOf(item.toUpperCase());
        }catch (Exception e){
            MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "Unknown item '" + item + "'");
            return;
        }
        warps.createSection(icon.toString());
        ConfigurationSection s = warps.getConfigurationSection(icon.toString());
        s.set("name", name);
        s.set("world", p.getWorld().getName());
        s.set("x", p.getLocation().getX());
        s.set("y", p.getLocation().getY());
        s.set("z", p.getLocation().getZ());
        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Warp " + name + " set.");
    }
}
