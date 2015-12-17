package me.koenn.kp.commands;


import org.bukkit.command.CommandSender;

abstract class HexicCommand{
    public abstract void onCommand(CommandSender sender, String[] args);

    private String message, usage;
    private String[] aliases;

    public HexicCommand(String message, String usage, String... aliases){
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }

    public final String getMessage() {
        return message;
    }

    public final String[] getAliases() {
        return aliases;
    }

    public final String getUsage() {
        return usage;
    }
}