package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.Ranks;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rankup extends HexicCommand {

    private Main main;
    private Ranks ranks;

    public Rankup(Main main, Ranks ranks) {
        super("Rankup", "/rankup", "ru");
        this.main = main;
        this.ranks = ranks;
    }

    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to be a player to do this");
            return;
        }
        Player p = (Player) sender;
        ranks.rankup(p);
    }
}