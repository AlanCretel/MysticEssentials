package fr.choco70.mysticessentials;

import fr.choco70.mysticessentials.commands.*;
import fr.choco70.mysticessentials.listeners.PlayerDeath;
import fr.choco70.mysticessentials.listeners.PlayerRespawn;
import fr.choco70.mysticessentials.listeners.PlayerJoin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MysticEssentials extends JavaPlugin {

    private HashMap<Player, Player> tpa = new HashMap<Player, Player>();
    private HashMap<Player, Player> tpahere = new HashMap<Player, Player>();

    @Override
    public void onEnable(){
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        try {
            config.save(this.getDataFolder().toString() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        createLanguageFile("fr_fr");
        FileConfiguration langFR_fr = YamlConfiguration.loadConfiguration(getLanguageFile("fr_fr"));
        langFR_fr.options().copyDefaults(true);

        try {
            langFR_fr.save(getLanguageFile("fr_fr"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        createLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us").toLowerCase());
        FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(getLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us").toLowerCase()));

        String configLoadedMessage = languageConfig.getString("CONFIG_LOADED", "§2MysticEssential: Configuration loaded.");
        getServer().getConsoleSender().sendMessage(configLoadedMessage);

        languageConfig.set("CONFIG_LOADED", configLoadedMessage);

        saveLanguageConfig(languageConfig, config.getString("SETTINGS.serverLanguage", "en_us").toLowerCase());

        this.getServer().getPluginManager().registerEvents(new PlayerJoin(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        this.getCommand("setspawn").setExecutor(new CommandSetSpawn());
        this.getCommand("spawn").setExecutor(new CommandSpawn());
        this.getCommand("delspawn").setExecutor(new CommandDelSpawn());
        this.getCommand("tpa").setExecutor(new CommandTpa());
        this.getCommand("tpaccept").setExecutor(new CommandTpAccept());
        this.getCommand("tpdeny").setExecutor(new CommandTpDeny());
        this.getCommand("tpahere").setExecutor(new CommandTpaHere());
        this.getCommand("sethome").setExecutor(new CommandSetHome());
        this.getCommand("home").setExecutor(new CommandHome());
        this.getCommand("delhome").setExecutor(new CommandDelHome());
        this.getCommand("back").setExecutor(new CommandBack());
        this.getCommand("setlanguage").setExecutor(new CommandSetLanguage());
        //this.getCommand("mysticessentialsreload").setExecutor(new CommandReload());
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MysticEssentials: Disabled");
    }

    public HashMap<Player, Player> getTpa(){
        return tpa;
    }

    public HashMap<Player, Player> getTpahere(){
        return tpahere;
    }

    public void toSpawn(Player player, String message){
        player.teleport(getSpawnLocation());
        if(message != null){
            player.sendMessage(message);
        }
    }

    public Location getSpawnLocation(){
        Location spawn = new Location(getServer().getWorld(getConfig().get("SPAWN.world").toString()), Double.valueOf(getConfig().get("SPAWN.x").toString()), Double.valueOf(getConfig().get("SPAWN.y").toString()), Double.valueOf(getConfig().get("SPAWN.z").toString()), Float.valueOf(getConfig().get("SPAWN.yaw").toString()), Float.valueOf(getConfig().get("SPAWN.pitch").toString()));
        return spawn;
    }

    public void createPlayerFile(String playerUUID){
        File playersForlder = new File(getDataFolder() + File.separator + "players" + File.separator);
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        if(!playersForlder.exists()){
            playersForlder.mkdir();
        }
        File file = new File(playersForlder,playerUUID + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getPlayerFile(String playerUUID){
        File playersForlder = new File(getDataFolder() + File.separator + "players" + File.separator);
        return new File(playersForlder,playerUUID + ".yml");
    }

    public void savePlayerConfig(FileConfiguration playerConfig, String playerUUID){
        try {
            playerConfig.save(getLanguageFile(playerUUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLanguageFile(String fileName){
        File langsForlder = new File(getDataFolder() + File.separator + "langs" + File.separator);
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        if(!langsForlder.exists()){
            langsForlder.mkdir();
        }
        File file = new File(langsForlder,fileName + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getLanguageFile(String fileName){
        File langsForlder = new File(getDataFolder() + File.separator + "langs" + File.separator);
        return new File(langsForlder,fileName + ".yml");
    }

    public void saveLanguageConfig(FileConfiguration languageConfig, String fileName){
        try {
            languageConfig.save(getLanguageFile(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
