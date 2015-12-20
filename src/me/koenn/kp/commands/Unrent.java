package me.koenn.kp.commands;

import me.koenn.kp.ConfigManager;
import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Unrent extends HexicCommand {

    private Main main;
    private ConfigManager cm;

    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            return;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        try{
                String name = (cm.get("c" + p.getUniqueId()).toString());
                ConfigurationSection ws = main.getConfig().getConfigurationSection(name);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg removemember " + name + " -w " + p.getWorld().getName() + " " + p.getName());
                World world = Bukkit.getWorld(ws.get("world").toString());
                Double x = ws.getDouble("x");
                Double y = ws.getDouble("y");
                Double z = ws.getDouble("z");
                Location signloc = new Location(world, x, y, z);
                Sign sign = (Sign) signloc.getBlock().getState();
                sign.setLine(0, ChatColor.DARK_BLUE + "[Cell]");
                sign.setLine(1, name);
                sign.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Empty");
                sign.setLine(3, ChatColor.GRAY + "" + ChatColor.BOLD + "Click to rent!");
                sign.update();
                ws.set("owner", "null");
                cm.set("c" + p.getUniqueId().toString(), null);
                main.saveConfig();
                MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Unrented " + name);
        }catch (Exception e){
            MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "You don't own a cell!");
        }
    }

    public Unrent(Main main) {
        super("Unrent your cell", "/unrent", "");
        this.main = main;
        this.cm = ConfigManager.getInstance();
    }
}