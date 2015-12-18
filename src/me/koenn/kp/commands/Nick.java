package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nick extends HexicCommand {

    private Main main;


    @Override
    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 0, 2, sender)) {
            Player p = Bukkit.getServer().getPlayer(args[0]);
            main.getConfig().getConfigurationSection("nicknames").createSection(p.getName());
            main.getConfig().getConfigurationSection("nicknames").set(p.getName(), args[1]);
            p.setDisplayName(args[0]);
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Set " + p.getName() + "'s nick to " + args[1]);
        } else {
            if (args.length == 1) {
                Player p = (Player) sender;
                main.getConfig().getConfigurationSection("nicknames").createSection(p.getName());
                main.getConfig().getConfigurationSection("nicknames").set(p.getName(), args[0]);
                p.setDisplayName(args[0]);
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Set " + p.getName() + "'s nick to " + args[0]);
            } else {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
            }
        }
    }

    public Nick(Main main) {
        super("Give yourself a nickname", "/nick [player] <nickname>", "");
        this.main = main;
    }
}