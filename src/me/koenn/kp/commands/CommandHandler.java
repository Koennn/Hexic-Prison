package me.koenn.kp.commands;

import me.koenn.kp.InventoryManager;
import me.koenn.kp.Main;
import me.koenn.kp.Ranks;
import me.koenn.kp.Warping;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.logging.Level;

public class CommandHandler implements CommandExecutor {


    private ArrayList<HexicCommand> cmds = new ArrayList<>();

    public void setup(Main main, Ranks ranks, InventoryManager ivm, Warping warping) {
        cmds.add(new Warn(main));
        cmds.add(new Adminmode(main));
        cmds.add(new Adminregister(main));
        cmds.add(new Chat(main));
        cmds.add(new Gotoserver(main));
        cmds.add(new History(main));
        cmds.add(new Hub(main));
        cmds.add(new Mute(main));
        cmds.add(new Nick(main));
        cmds.add(new Relog(main));
        cmds.add(new Help(main));
        cmds.add(new Rankup(main, ranks));
        cmds.add(new Setrank(main, ranks));
        cmds.add(new Emoney(main));
        cmds.add(new Bal(main));
        cmds.add(new Pay(main));
        cmds.add(new Warp(main, ivm));
        cmds.add(new Setwarp(main, warping));
        cmds.add(new Unrent(main));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Bukkit.getLogger().log(Level.INFO, "Execute " + cmd.getName());

        HexicCommand c = getHexicCommand(cmd.getName());

        if (c != null) {
            c.onCommand(sender, args);
        }

        return true;
    }

    private HexicCommand getHexicCommand(String name) {
        for (HexicCommand cmd : cmds) {
            if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
        }
        return null;
    }
}