package fr.choco70.mysticessentials;

import fr.choco70.mysticessentials.commands.*;
import fr.choco70.mysticessentials.listeners.PlayerDeath;
import fr.choco70.mysticessentials.listeners.PlayerJoin;
import fr.choco70.mysticessentials.listeners.PlayerRespawn;
import fr.choco70.mysticessentials.tabCompleters.*;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import fr.choco70.mysticessentials.utils.rulesManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;

public class MysticEssentials extends JavaPlugin {

    private HashMap<Player, Player> tpa = new HashMap<>();
    private HashMap<Player, Player> tpahere = new HashMap<>();

    private HashMap<Player, Player> conversations = new HashMap<>();

    private HashMap<Player, Integer> delays_tpa = new HashMap<>();
    private HashMap<Player, Integer> delays_tpahere = new HashMap<>();
    private HashMap<Player, Integer> delays_home = new HashMap<>();
    private HashMap<Player, Integer> delays_back = new HashMap<>();
    private HashMap<Player, Integer> delays_spawn = new HashMap<>();

    private langsManager langsManager;
    private playersManager playersManager;
    private rulesManager rulesManager;

    @Override
    public void onEnable(){
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        try {
            config.save(this.getDataFolder().toString() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupManagers();

        rulesManager.createRulesFile();

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticEssentials: Rules file loaded");

        langsManager.setupDefaultLanguages();
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        langsManager.createLanguageFile(serverLanguage);
        langsManager.updateLanguagesConfigs();

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticEssentials: Config loaded");

        this.getServer().getPluginManager().registerEvents(new PlayerJoin(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        setupCommands();
        setupAutoComplete();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticEssentials: Plugin enabled");
    }

    @Override
    public void onDisable(){
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "MysticEssentials: Disabled");
    }

    private void setupManagers(){
        langsManager = new langsManager();
        playersManager = new playersManager();
        rulesManager = new rulesManager();
    }

    private void setupCommands(){
        //No dependencies commands
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
        this.getCommand("homelist").setExecutor(new CommandHomeList());
        this.getCommand("setwarp").setExecutor(new CommandSetWarp());
        this.getCommand("warp").setExecutor(new CommandWarp());
        this.getCommand("delwarp").setExecutor(new CommandDelWarp());
        this.getCommand("warplist").setExecutor(new CommandWarpList());
        this.getCommand("feed").setExecutor(new CommandFeed());
        this.getCommand("fly").setExecutor(new CommandFly());
        this.getCommand("msg").setExecutor(new CommandMsg());
        this.getCommand("reply").setExecutor(new CommandReply());
        this.getCommand("ignore").setExecutor(new CommandIgnore());
        this.getCommand("ignorelist").setExecutor(new CommandIgnoreList());
        this.getCommand("broadcast").setExecutor(new CommandBroadcast());
        this.getCommand("setdefaulthome").setExecutor(new CommandSetDefaultHome());
        this.getCommand("rules").setExecutor(new CommandRules());
        this.getCommand("addrule").setExecutor(new CommandAddRule());
        this.getCommand("setrule").setExecutor(new CommandSetRule());
        this.getCommand("delrule").setExecutor(new CommandDelRule());
        this.getCommand("rule").setExecutor(new CommandRule());
        this.getCommand("discord").setExecutor(new CommandDiscord());
        this.getCommand("website").setExecutor(new CommandWebsite());
        this.getCommand("day").setExecutor(new CommandsTime());
        this.getCommand("night").setExecutor(new CommandsTime());
        this.getCommand("sun").setExecutor(new CommandsWeather());
        this.getCommand("rain").setExecutor(new CommandsWeather());
        this.getCommand("storm").setExecutor(new CommandsWeather());
        this.getCommand("tpall").setExecutor(new CommandTpAll());
        //this.getCommand("vanish").setExecutor(new CommandVanish());
        //this.getCommand("mysticessentialsreload").setExecutor(new CommandReload());
        //Vault required commands
        if(getServer().getPluginManager().isPluginEnabled("Vault")){
            this.getCommand("balance").setExecutor(new CommandBalance());
        }
    }

    private void setupAutoComplete(){
        this.getCommand("home").setTabCompleter(new homeTabCompleter());
        this.getCommand("delhome").setTabCompleter(new homeTabCompleter());
        this.getCommand("sethome").setTabCompleter(new homeTabCompleter());
        this.getCommand("setdefaulthome").setTabCompleter(new homeTabCompleter());
        this.getCommand("msg").setTabCompleter(new msgCompleter());
        this.getCommand("reply").setTabCompleter(new noCompleter());
        this.getCommand("broadcast").setTabCompleter(new noCompleter());
        this.getCommand("rules").setTabCompleter(new noCompleter());
        this.getCommand("addrule").setTabCompleter(new noCompleter());
        this.getCommand("setrule").setTabCompleter(new rulesCompleter());
        this.getCommand("delrule").setTabCompleter(new rulesCompleter());
        this.getCommand("rule").setTabCompleter(new rulesCompleter());
        this.getCommand("setlanguage").setTabCompleter(new languageTabCompleter());
    }

    public HashMap<Player, Player> getTpa(){
        return tpa;
    }

    public HashMap<Player, Player> getTpahere(){
        return tpahere;
    }

    public HashMap<Player, Player> getConversations(){
        return conversations;
    }

    public void toSpawn(Player player, String message){
        player.teleport(getSpawnLocation());
        if(message != null){
            player.sendMessage(message);
        }
    }

    public Location getSpawnLocation(){
        return new Location(getServer().getWorld(getConfig().get("SPAWN.world").toString()), Double.valueOf(getConfig().get("SPAWN.x").toString()), Double.valueOf(getConfig().get("SPAWN.y").toString()), Double.valueOf(getConfig().get("SPAWN.z").toString()), Float.valueOf(getConfig().get("SPAWN.yaw").toString()), Float.valueOf(getConfig().get("SPAWN.pitch").toString()));
    }

    public langsManager getLangsManager(){
        return this.langsManager;
    }

    public playersManager getPlayersManager(){
        return this.playersManager;
    }

    public rulesManager getRulesManager(){
        return this.rulesManager;
    }
}
