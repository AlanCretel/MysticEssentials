package fr.choco70.mysticessentials.listeners;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String playerUUID = player.getUniqueId().toString();
        String playerDisplayName = player.getDisplayName();

        plugin.createPlayerFile(playerUUID);
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));

        String playerLocale = playerConfig.getString("language", "en_us");
        playerConfig.set("language", playerLocale);

        plugin.createLanguageFile(playerLocale);
        FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerLocale));

        String welcome_back_message = languageConfig.getString("WELCOME_BACK_MESSAGE", "§6Welcome back on §4#server_name#§6, §7#player#§6 !");
        languageConfig.set("WELCOME_BACK_MESSAGE", welcome_back_message);

        if(!playerConfig.isSet("name")){
            playerConfig.set("name", player.getName());
            playerConfig.set("display_name", playerDisplayName);

            String welcome_message = languageConfig.getString("WELCOME_MESSAGE", "Welcome on #server_name# !");
            languageConfig.set("WELCOME_MESSAGE", welcome_message);

            if(config.isSet("SPAWN.x") && config.isSet("SPAWN.y") && config.isSet("SPAWN.z") && config.isSet("SPAWN.pitch") && config.isSet("SPAWN.yaw") && config.isSet("SPAWN.world")){
                plugin.toSpawn(player, formatString(welcome_message, player));
            }
            else{
                player.sendMessage(formatString(welcome_message, player));
            }

            String welcome_broadcast = languageConfig.getString("WELCOME_BROADCAST", "Welcome to #player# on #server_name# !");
            languageConfig.set("WELCOME_BROADCAST", welcome_broadcast);
            plugin.getServer().broadcastMessage(formatString(welcome_broadcast, player));
        }
        else if(playerConfig.get("name").toString() == player.getName()){
            playerConfig.set("name", player.getName());
        }
        else if(config.getBoolean("SETTINGS.spawnOnJoin", false)){
            if(config.isSet("SPAWN.x") && config.isSet("SPAWN.y") && config.isSet("SPAWN.z") && config.isSet("SPAWN.pitch") && config.isSet("SPAWN.yaw") && config.isSet("SPAWN.world")){
                plugin.toSpawn(player, formatString(welcome_back_message, player));
            }
        }
        else{
            player.sendMessage(formatString(welcome_back_message, player));
        }

        plugin.savePlayerConfig(playerConfig, playerUUID);
        plugin.saveLanguageConfig(languageConfig, playerLocale);
    }

    public String formatString(String string, Player player){
        String player_placeholder = "#player#";
        String serverName_placeholder = "#server_name#";

        String formatedString = string;
        formatedString = formatedString.replaceAll(player_placeholder, player.getName());
        formatedString = formatedString.replaceAll(serverName_placeholder, config.getString("SETTINGS.serverName", "A minecraft server"));

        return formatedString;
    }
}
