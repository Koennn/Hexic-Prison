package me.koenn.kp;

import me.koenn.kp.commands.MessageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Ranks {

    public ArrayList<Integer> prices = new ArrayList<>();
    private ConfigManager cm;
    private Main main;
    private Money money;

    public Ranks(Main main) {
        this.cm = ConfigManager.getInstance();
        this.main = main;
        this.money = new Money(main);

        this.prices.clear();
        ConfigurationSection rp = main.getConfig().getConfigurationSection("rankprices");
        rp.getKeys(false).stream().filter(key -> !(key.equals("enabled"))).forEach(key -> this.prices.add(rp.getInt(key)));
    }

    public void setRank(Player p, PrisonRank r) {
        cm.set(p.getUniqueId().toString(), r.ordinal());
        MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "You are now rank " + r.toString());
        main.log("Set " + p.getName() + "'s rank to " + r.toString());
    }

    public PrisonRank getRank(Player p) {
        int i = cm.getInt(p.getUniqueId().toString());
        return PrisonRank.fromValue(i);
    }

    public void rankup(Player p) {
        try {
            int i;
            if (cm.get(p.getUniqueId().toString()) == null) {
                i = 0;
            } else {
                if (cm.getInt(p.getUniqueId().toString()) == 26) {
                    MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "You are already the highest rank!");
                    return;
                }
                i = (cm.getInt(p.getUniqueId().toString()) + 1);
            }
            if (getRankPrice(i) == null) {
                MessageManager.getInstance().msg(p, MessageManager.MessageType.WARN, "An error occurred, please contact Koenn.");
                return;
            }
            if (money.removeFrom(p, getRankPrice(i))) {
                cm.set(p.getUniqueId().toString(), i);
                MessageManager.getInstance().msg(p, MessageManager.MessageType.INFO, "You are now rank " + PrisonRank.fromValue(i));
                main.log("Set " + p.getName() + "'s rank to " + PrisonRank.fromValue(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            main.log(" [ERROR] Unable to cast " + cm.get(p.getUniqueId().toString()) + " to int");
        }
    }

    public Integer getRankPrice(Integer rank) {
        if (rank >= prices.size()) {
            main.log("Unknown rank " + PrisonRank.fromValue(rank).toString());
            return null;
        }
        return this.prices.get(rank);
    }

    public enum PrisonRank {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

        private final int value;

        PrisonRank() {
            this.value = ordinal();
        }

        public static PrisonRank fromValue(int value) throws IllegalArgumentException {
            try {
                return PrisonRank.values()[value];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Unknown rank value: " + value);
            }
        }
    }

}
