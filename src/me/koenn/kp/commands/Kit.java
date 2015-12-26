package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.command.CommandSender;

public class Kit extends HexicCommand {

    private Main main;

    public Kit(Main main) {
        super("Get a kit", "/kit", "");
        this.main = main;
    }

    public void onCommand(CommandSender sender, String[] args) {

    }
}