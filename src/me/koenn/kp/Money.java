package me.koenn.kp;

import me.koenn.kp.commands.MessageManager;
import org.bukkit.entity.Player;

public class Money {

    private Main main;
    private ConfigManager cm;

    public Money(Main main){
        this.main = main;
        this.cm = ConfigManager.getInstance();
    }

    public Integer getBalance(Player p){
        int bal;
        try{
            bal = cm.getInt("bal" + p.getUniqueId());
        }catch (Exception e){
            cm.set("bal" + p.getUniqueId(), 0);
            bal = 0;
        }
        return bal;
    }

    public void setBalance(Player p, Integer amount){
        cm.set("bal" + p.getUniqueId(), amount);
        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "Your balance has been set to $" + amount);
    }

    public void addTo(Player p, Integer amount){
        try{
            cm.set("bal" + p.getUniqueId(), (getBalance(p) + amount));
            MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "$" + amount + " has been added to your balance");
        }catch (Exception e){
            cm.set("bal" + p.getUniqueId(), amount);
            MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "$" + amount + " has been added to your balance");
        }
    }

    public boolean removeFrom(Player p, Integer amount){
        try{
            if(getBalance(p) >= amount){
                cm.set("bal" + p.getUniqueId(), (getBalance(p) - amount));
                MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "$" + amount + " has been removed from your balance");
                return true;
            } else {
                MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "You don't have enough money to do that");
                return false;
            }
        }catch (Exception e){
            MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "You don't have enough money to do that");
            return false;
        }
    }

}
