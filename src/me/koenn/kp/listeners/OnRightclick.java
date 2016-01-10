package me.koenn.kp.listeners;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.koenn.kp.Cells;
import me.koenn.kp.Main;
import me.koenn.kp.Money;
import me.koenn.kp.commands.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OnRightclick implements Listener {

    private Main main;
    private Money money;

    public OnRightclick(Main main, Money money) {
        this.main = main;
        this.money = money;
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

    @EventHandler
    public void onRightclick(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            if (p.getInventory().getItemInHand().equals(serverSelector())) {
                e.setCancelled(true);
                main.ivm.openServerSelector(p);
            }
            if (p.getInventory().getItemInHand().equals(helpMenu())) {
                e.setCancelled(true);
                p.performCommand("help");
            }
            if (p.getInventory().getItemInHand().equals(serverInfo())) {
                e.setCancelled(true);
                main.ShowServerInfo(p);
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
                    main.log("Click");
                    Cells cells = new Cells(main, money);
                    cells.clickSign(e.getClickedBlock(), p);
                }
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location loc = e.getClickedBlock().getLocation();
                Vector v = new Vector(loc.getX(), loc.getBlockY(), loc.getZ());
                ApplicableRegionSet set = Main.wg.getRegionManager(loc.getWorld()).getApplicableRegions(v);
                Iterator<ProtectedRegion> iter = set.iterator();
                ProtectedRegion region = null;
                while (iter.hasNext()) {
                    ProtectedRegion nextRegion = iter.next();
                    if (region == null || region.getPriority() > region.getPriority()) region = nextRegion;
                }
                try {
                    region.getId();
                } catch (Exception ex) {
                    return;
                }
                if (region.getId().contains("cell") && !region.getMembers().contains(p.getUniqueId())) {
                    if (!p.isOp()) {
                        e.setCancelled(true);
                        MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "This is not your cell!");
                    }
                }
            }
        } catch (Exception ex) {
            main.catchEvent(ex, e.getPlayer(), e.getEventName());
        }
    }

}