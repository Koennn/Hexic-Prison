package me.koenn.kp.commands;

import me.koenn.kp.ConfigManager;
import me.koenn.kp.Main;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Hub extends HexicCommand {

    private Main main;
    private ConfigManager cm;

    public Hub(Main main) {
        super("Go to the hub", "/hub", "h");
        this.main = main;
        this.cm = ConfigManager.getInstance();
    }

    private ItemStack serverSelector() {
        ItemStack i = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Server Selector");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click to select a server!");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    private ItemStack helpMenu() {
        ItemStack i = new ItemStack(Material.DIAMOND);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Help Menu");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click open the help menu");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    private ItemStack serverInfo() {
        ItemStack i = new ItemStack(Material.EMERALD);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Server Info");
        List<String> l = new ArrayList<>();
        l.add(ChatColor.GREEN + "Click see server information");
        im.setLore(l);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);
        return i;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player s = (Player) sender;
        World world = Bukkit.getWorld(cm.get("hw").toString());
        Double x = cm.get("hx");
        Double y = cm.get("hx");
        Double z = cm.get("hx");
        Float yaw = cm.get("hyw");
        Float pitch = cm.get("hph");
        s.teleport(new Location(world, x, y, z, yaw, pitch));
        MessageManager.getInstance().msg(sender, MessageManager.MessageType.INFO, "Teleported to the hub");
        s.getInventory().setItem(2, helpMenu());
        s.getInventory().setItem(4, serverSelector());
        s.getInventory().setItem(6, serverInfo());
        s.getInventory().setHeldItemSlot(4);
    }
}