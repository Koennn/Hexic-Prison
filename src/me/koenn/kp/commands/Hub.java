package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hub extends HexicCommand {

    private Main main;


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        s.performCommand("tohub");
        s.getInventory().setItem(2, main.helpMenu());
        s.getInventory().setItem(4, main.serverSelector());
        s.getInventory().setItem(6, main.serverInfo());
        s.getInventory().setHeldItemSlot(4);
    }

    public Hub(Main main) {
        super("Go to the hub", "/hub", "h");
        this.main = main;
    }
}