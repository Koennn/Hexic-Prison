package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.command.CommandSender;

public class Help extends HexicCommand {

    private Main main;

    public void onCommand(CommandSender sender, String[] args) {
        //TODO: Add code
    }

    public Help(Main main) {
        super("", "", "");
        this.main = main;
    }
}