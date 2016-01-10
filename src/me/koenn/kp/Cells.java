package me.koenn.kp;

import me.koenn.kp.commands.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Cells {

    private ConfigManager cm;
    private Main main;
    private Money money;

    public Cells(Main main, Money money) {
        cm = ConfigManager.getInstance();
        this.main = main;
        this.money = money;
    }

    public void clickSign(Block b, Player p) {
        if (b.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) b.getState();
            if (sign.getLine(0).contains(ChatColor.DARK_BLUE + "[Cell]")) {
                String name = sign.getLine(1).toLowerCase();
                ConfigurationSection ws = main.getConfig().getConfigurationSection(name);
                if (ws.getBoolean("setup")) {
                    if (ws.get("owner") != "null") {
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "This cell is already rented");
                        return;
                    }
                    if (money.removeFrom(p, 500)) {
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "You rented " + sign.getLine(1) + " for $500 every day");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg addmember " + name + " -w " + p.getWorld().getName() + " " + p.getName());
                        ws.set("owner", p.getUniqueId().toString());
                        main.saveConfig();
                        sign.setLine(2, ChatColor.DARK_RED + "" + ChatColor.BOLD + p.getName());
                        sign.setLine(3, "");
                        cm.set("c" + p.getUniqueId().toString(), name);
                        sign.update();
                    }
                    return;
                }
                if (p.isOp()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + sign.getLine(1) + " -w " + p.getWorld().getName() + " build deny");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + sign.getLine(1) + " -w " + p.getWorld().getName() + " block-break deny");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + sign.getLine(1) + " -w " + p.getWorld().getName() + " chest-access allow");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag " + sign.getLine(1) + " -w " + p.getWorld().getName() + " interact allow");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg priority " + sign.getLine(1) + " -w " + p.getWorld().getName() + " 11");
                    ws.set("tworld", p.getLocation().getWorld().getName());
                    ws.set("tx", p.getLocation().getX());
                    ws.set("ty", p.getLocation().getY());
                    ws.set("tz", p.getLocation().getZ());
                    ws.set("setup", true);
                    main.saveConfig();
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, name + " is now setup!");
                    main.log("Player " + p + " setup " + name);
                }
            }
        }
    }
}
