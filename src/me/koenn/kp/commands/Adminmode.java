package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Adminmode extends HexicCommand {

    private Main main;

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        if(args.length == 1){
            if(main.getConfig().getConfigurationSection("passwords").contains(s.getUniqueId().toString())){
                main.admins.remove(s.getUniqueId());
                main.admins.put(s.getUniqueId(), main.getConfig().getConfigurationSection("passwords").get(s.getUniqueId().toString()).toString());
            }
            if(!(s.isOp())){
                if (main.admins.containsKey(s.getUniqueId())) {
                    if (main.admins.get(s.getUniqueId()).equals(args[0])) {
                        s.setOp(true);
                        MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Enabled adminmode for player '" + s.getName() + "'");
                        s.performCommand("gm 1");
                        s.performCommand("speed fly 2");
                    } else {
                        MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "Incorrect password, please try again!");
                    }
                } else {
                    MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You are not an admin!");
                }
            } else {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You are already logged in!");
            }
        } else {
            if(main.admins.containsKey(s.getUniqueId())) {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "Please specify a password!");
            } else {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You are not an admin!");
            }
        }
    }

    public Adminmode(Main main) {
        super("Login to adminmode", "/adminmode <password>", "am");
        this.main = main;
    }
}