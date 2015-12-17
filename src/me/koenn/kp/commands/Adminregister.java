package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Adminregister extends HexicCommand {

    private Main main;


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        if(args.length == 1){
            if(s.isOp()){
                main.admins.put(s.getUniqueId(), args[0]);
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Registered username '" + s.getName() + "' with password '" + args[0] + "'");
                main.getConfig().getConfigurationSection("passwords").createSection(s.getUniqueId().toString());
                main.getConfig().getConfigurationSection("passwords").set(s.getUniqueId().toString(), args[0]);
                main.saveConfig();
            } else {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to be opped to do this!");
            }
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "Please specify a password!");
        }
    }

    public Adminregister(Main main) {
        super("Register as an admin", "/adminregister <password>", "");
        this.main = main;
    }
}