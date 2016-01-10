package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Money;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pay extends HexicCommand {

    private Main main;
    private Money money;

    public Pay(Main main) {
        super("Pay a player", "/pay <player> <amount>", "");
        this.main = main;
        this.money = new Money(main);
    }

    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 0, 2, sender)) {
            Player a = Bukkit.getPlayer(args[0]);
            Player r = Bukkit.getPlayer(((Player) sender).getUniqueId());
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (Exception e) {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
                return;
            }
            if (money.removeFrom(r, amount)) {
                money.addTo(a, amount);
            }
            if (a == r) {
                MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "That seems useful!");
            }
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }
}