package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class History extends HexicCommand {

    private Main main;


    @Override
    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        if (main.checkPlayer(args, 0, 1, sender)) {
            Player p = Bukkit.getServer().getPlayer(args[0]);
            s.openInventory(main.ivm.playerInfoInit(p, s));
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Loading history from " + args[0]);
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }

    public History(Main main) {
        super("Get information about a player", "/history <player>", "his");
        this.main = main;
    }
}