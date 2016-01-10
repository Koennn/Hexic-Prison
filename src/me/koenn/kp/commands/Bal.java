package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Money;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bal extends HexicCommand {

    private Main main;
    private Money money;

    public Bal(Main main) {
        super("Check your balance", "/bal [player]", "/balance");
        this.main = main;
        this.money = new Money(main);
    }

    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 0, 1, sender)) {
            Player p = Bukkit.getPlayer(args[0]);
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Balance of " + p.getName() + ": $" + money.getBalance(p).toString());
        } else {
            Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Balance: $" + money.getBalance(p).toString());
        }
    }
}