package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Money;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Emoney extends HexicCommand {

    private Main main;
    private Money money;

    public Emoney(Main main) {
        super("General economy command", "/emoney <add/remove/set> <player> <amount>", "em");
        this.main = main;
        this.money = new Money(main);
    }

    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 1, 3, sender)) {
            Player p = Bukkit.getPlayer(args[1]);
            switch (args[0]) {
                case "add":
                    try {
                        money.addTo(p, Integer.parseInt(args[2]));
                    } catch (Exception e) {
                        MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
                    }
                    break;
                case "remove":
                    try {
                        money.removeFrom(p, Integer.parseInt(args[2]));
                    } catch (Exception e) {
                        MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
                    }
                    break;
                case "set":
                    try {
                        money.setBalance(p, Integer.parseInt(args[2]));
                    } catch (Exception e) {
                        MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
                    }
                    break;
                default:
                    MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
                    break;
            }
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }
}