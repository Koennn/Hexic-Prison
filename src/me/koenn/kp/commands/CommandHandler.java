package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.logging.Level;

public class CommandHandler implements CommandExecutor{


    private ArrayList<HexicCommand> cmds = new ArrayList<>();

    public void setup(Main main){
        cmds.add(new Warn(main));
        cmds.add(new Adminmode(main));
        cmds.add(new Adminregister(main));
        cmds.add(new Chat(main));
        cmds.add(new Gotoserver(main));
        cmds.add(new History(main));
        cmds.add(new Hub(main));
        cmds.add(new Mute(main));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        Bukkit.getLogger().log(Level.INFO, "Execute");

        HexicCommand c = getHexicCommand(cmd.getName());

        if(c != null){
            c.onCommand(sender, args);
        }

        return true;
    }

    private HexicCommand getHexicCommand(String name){
        Bukkit.getLogger().log(Level.INFO, "Get command");
        for(HexicCommand cmd: cmds){
            if(cmd.getClass().getSimpleName().equalsIgnoreCase(name))return cmd;
        }
        return null;
    }
}