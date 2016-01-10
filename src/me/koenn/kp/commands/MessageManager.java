package me.koenn.kp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    private static MessageManager instance = new MessageManager();
    private String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "[Hexic] " + ChatColor.RESET;

    public MessageManager() {

    }

    public static MessageManager getInstance() {
        return instance;
    }

    public void msg(CommandSender sender, MessageType type, String... messages) {
        for (String msg : messages) {
            sender.sendMessage(prefix + type.getColor() + msg);
        }
    }

    public enum MessageType {

        INFO(ChatColor.GREEN),
        WARN(ChatColor.RED);

        private ChatColor color;

        MessageType(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }
}
