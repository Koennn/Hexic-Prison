package me.koenn.kp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {

    private static ConfigManager instance = new ConfigManager();

    public static ConfigManager getInstance(){
        return instance;
    }

    private Main main;

    public FileConfiguration data;
    private File datafile;

    public void setup(Plugin p, Main main){
        this.main = main;

        if (!p.getDataFolder().exists()){
            p.getDataFolder().mkdir();
            main.getConfig().options().copyDefaults(true);
            main.saveConfig();
        }

        datafile = new File(p.getDataFolder(), "data.yml");

        if (!datafile.exists()) {
            try { datafile.createNewFile(); }
            catch (Exception e) {
                main.log("[ERROR] Failed to create data file, disabling plugin...");
                main.disablePlugin(e.toString());
            }
        }

        data = YamlConfiguration.loadConfiguration(datafile);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        try {
            return (T) data.get(path);
        }catch (Exception e){
            main.log("[ERROR] Unable to load data, please contact Koenn.");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getFromConfig(String path) {
        try {
            return (T) main.getConfig().get(path);
        }catch (Exception e){
            main.log("[ERROR] Unable to load from config, please contact Koenn.");
            return null;
        }
    }

    public Integer getInt(String path){
        try {
            return Integer.parseInt(data.get(path).toString());
        }catch (Exception e){
            main.log("[ERROR] Unable to load data, please contact Koenn.");
            return null;
        }
    }

    public void set(String path, Object value) {
        data.set(path, value);
        try { data.save(datafile); }
        catch (Exception e) {
            main.log("[ERROR] Unable to save data, please contact Koenn.");
        }
    }

    public void setInConfig(String path, Object value) {
        main.getConfig().set(path, value);
        try { main.saveConfig(); }
        catch (Exception e) {
            main.log(" [ERROR] Unable to save to config, please contact Koenn.");
        }
    }
}
