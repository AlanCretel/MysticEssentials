package fr.choco70.mysticessentials.listeners;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String playerDisplayName = player.getDisplayName();

        String serverLanguage = localesManager.getServerLocale();
        String playerLocale = sqLiteManager.getPlayerLocale(player.getUniqueId());

        String welcome_back_message = localesManager.getMessage(playerLocale, "WELCOME_BACK_MESSAGE");

        if(!sqLiteManager.playerExist(player.getUniqueId())){
            String welcome_message = localesManager.getMessage(playerLocale, "WELCOME_MESSAGE");

            if(config.isSet("SPAWN.x") && config.isSet("SPAWN.y") && config.isSet("SPAWN.z") && config.isSet("SPAWN.pitch") && config.isSet("SPAWN.yaw") && config.isSet("SPAWN.world")){
                plugin.toSpawn(player, formatString(welcome_message, player));
            }
            else{
                player.sendMessage(formatString(welcome_message, player));
            }

            String welcome_broadcast = localesManager.getMessage(serverLanguage, "WELCOME_BROADCAST");
            plugin.getServer().broadcastMessage(formatString(welcome_broadcast, player));
            sqLiteManager.insertPlayer(player.getUniqueId(), player.getName(), playerDisplayName, playerLocale);
        }

        if(config.getBoolean("SETTINGS.spawnOnJoin", false)){
            if(config.isSet("SPAWN.x") && config.isSet("SPAWN.y") && config.isSet("SPAWN.z") && config.isSet("SPAWN.pitch") && config.isSet("SPAWN.yaw") && config.isSet("SPAWN.world")){
                plugin.toSpawn(player, formatString(welcome_back_message, player));
            }
        }
        else{
            player.sendMessage(formatString(welcome_back_message, player));
        }
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
