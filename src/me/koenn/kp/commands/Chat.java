package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat extends HexicCommand {

    private Main main;


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        if (main.mode.get(s) == "server") {
            main.mode.remove(s);
            main.mode.put(s, "global");
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "You are now listening to " + ChatColor.YELLOW + ChatColor.BOLD + "Global" + ChatColor.GREEN + " chat.");
        } else if(main.mode.get(s) == "global") {
            main.mode.remove(s);
            main.mode.put(s, "server");
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "You are now listening to " + ChatColor.YELLOW + ChatColor.BOLD + "Server" + ChatColor.GREEN + " chat.");
        } else {
            s.kickPlayer(ChatColor.BLUE + "" + ChatColor.BOLD + "Please relog!");
        }
    }

    public Chat(Main main) {
        super("Change chat mode", "/chat", "ct");
        this.main = main;
    }
}