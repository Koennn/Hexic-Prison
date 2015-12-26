package me.koenn.kp.commands;

import me.koenn.kp.ConfigManager;
import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sethub extends HexicCommand {

    private Main main;
    private ConfigManager cm;

    public Sethub(Main main) {
        super("Set one or multiple hub locations", "/sethub <s/m>", "");
        this.main = main;
        this.cm = ConfigManager.getInstance();
    }

    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to do this.");
            return;
        }
        if (args.length != 1) {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        if (args[0] == "s") {
            cm.set("ht", HubType.SINGLE);
            String world = p.getWorld().getName();
            Double x = p.getLocation().getX();
            Double y = p.getLocation().getY();
            Double z = p.getLocation().getZ();
            Float yaw = p.getLocation().getYaw();
            Float pitch = p.getLocation().getPitch();
            cm.set("hw", world);
            cm.set("hx", x);
            cm.set("hy", y);
            cm.set("hz", z);
            cm.set("hyw", yaw);
            cm.set("hph", pitch);
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Hub location set.");
        } else if (args[0] == "m") {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "We dont support multiple mode yet.");
            //TODO: Add multiple mode.
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }

    public enum HubType {
        SINGLE, MULTIPLE
    }
}