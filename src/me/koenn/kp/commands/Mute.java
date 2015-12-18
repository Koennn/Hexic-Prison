package me.koenn.kp.commands;

import me.koenn.kp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Mute extends HexicCommand{

    private Main main;

    public void addMute(Player p){
        ConfigurationSection w = main.getConfig().getConfigurationSection("mutes");
        Integer a;
        try{
            a = w.getInt(p.getUniqueId().toString());
        } catch (Exception ex){
            w.createSection(p.getUniqueId().toString());
            a = 0;
        }
        w.set(p.getUniqueId().toString(), (a + 1));
        main.saveConfig();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCommand(CommandSender sender, String[] args) {
        if (main.checkPlayer(args, 0, 3, sender)){
            Integer time;
            if(args[1] != null){
                try {
                    time = Integer.parseInt(args[1]);
                } catch (Exception ex){
                    MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, args[1] + " is not a number!");
                    return;
                }
                if(args[2] != null){
                    switch (args[2]){
                        case "m":
                            time = (time*60);
                            break;
                        case "h":
                            time = (time*3600);
                            break;
                        case "d":
                            if(time == 1){
                                time = ((time*3600)*24);
                            } else {
                                MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You cannot mute for longer than 1 day!");
                                return;
                            }
                            break;
                    }
                } else {
                    MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, "You need to specify m/h/d");
                    return;
                }
            } else {
                time = null;
            }
            Player p = Bukkit.getServer().getPlayer(args[0]);
            if(time == null){
                if (main.mute.containsKey(p)) {
                    main.mute.remove(p);
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.DARK_GRAY + " was unmuted by" + ChatColor.YELLOW + " " + ChatColor.BOLD + sender.getName());
                } else {
                    main.mute.put(p, true);
                    addMute(p);
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.DARK_GRAY + " was muted by" + ChatColor.YELLOW + " " + ChatColor.BOLD + sender.getName());
                }
            } else {
                if (!(main.mute.containsKey(p))){
                    main.mute.put(p, true);
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player" + ChatColor.YELLOW + " " + ChatColor.BOLD + p.getName() + ChatColor.DARK_GRAY + " was muted by" + ChatColor.YELLOW + " " + ChatColor.BOLD + sender.getName() + " for " + args[1] + args[2]);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> main.mute.remove(p), (20*time));
                }
            }
        } else {
            MessageManager.getInstance().msg(sender, MessageManager.MessageType.WARN, super.getUsage());
        }
    }

    public Mute(Main main) {
        super("Mute or unmute a player", "/mute <player> <time> <s/m/d>", "");
        this.main = main;
    }
}
