package me.koenn.kp.commands;

import me.koenn.kp.Main;
import me.koenn.kp.SettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help extends HexicCommand {

    private Main main;

    public Help(Main main) {
        super("Open help menu", "/help", "");
        this.main = main;
    }

    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to be a player to do this");
            return;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        SettingsMenu menu = new SettingsMenu(main);
        menu.openMenu(p);
    }
}