package me.koenn.kp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    public enum MessageType{

        INFO(ChatColor.GREEN),
        WARN(ChatColor.RED);

        private ChatColor color;

        MessageType(ChatColor color){
            this.color = color;
        }

        public ChatColor getColor(){
            return color;
        }
    }

    public MessageManager(){

    }

    private static MessageManager instance = new MessageManager();

    public static MessageManager getInstance(){
        return instance;
    }

    private String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "[Hexic] " + ChatColor.RESET;

    public void msg(CommandSender sender, MessageType type, String... messages){
        for(String msg : messages){
            sender.sendMessage(prefix + type.getColor() + msg);
        }
    }
}
