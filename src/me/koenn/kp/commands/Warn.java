package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Warn extends HexicCommand {

    private Main main;

    public Warn(Main main) {
        super("Warn a player", "/warn <player> <warning>", "w");
        this.main = main;
    }

    public void addWarning(Player p) {
        ConfigurationSection w = main.getConfig().getConfigurationSection("warns");
        Integer a;
        try {
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex) {
            w.createSection(p.getUniqueId().toString());
            a = 0;
        }
        w.set(p.getUniqueId().toString(), (a + 1));
        main.saveConfig();
    }

    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        Bukkit.getLogger().log(Level.INFO, "Run");
        if (main.checkPlayer(args, 0, 2, sender)) {
            Player p = Bukkit.getServer().getPlayer(args[0]);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String message = sb.toString().trim();
            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Warning from " + sender.getName() + ": " + message);
            addWarning(p);
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }
}