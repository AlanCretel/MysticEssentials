package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerUUID = player.getUniqueId().toString();

            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));
            FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));

            if(playerConfig.isSet("last_location.world")){
                String teleportToLastLocationMessage = playerLanguageConfig.getString("TELEPORT_LAST_LOCATION", "Teleporting to your last location...");
                playerLanguageConfig.set("TELEPORT_LAST_LOCATION", teleportToLastLocationMessage);

                player.sendMessage(teleportToLastLocationMessage);
                Location lastLocation = new Location(plugin.getServer().getWorld(playerConfig.get("last_location.world").toString()), playerConfig.getDouble("last_location.x"), playerConfig.getDouble("last_location.y"), playerConfig.getDouble("last_location.z"), Float.valueOf(playerConfig.getString("last_location.yaw","0")), Float.valueOf(playerConfig.getString("last_location.pitch","0")));
                player.teleport(lastLocation);
            }
            else{
                String noLastLocationMessage = playerLanguageConfig.getString("NO_LAST_LOCATION", "You don't have any last location registered.");
                playerLanguageConfig.set("NO_LAST_LOCATION", noLastLocationMessage);

                player.sendMessage(noLastLocationMessage);
            }
            plugin.saveLanguageConfig(playerLanguageConfig, playerConfig.getString("language", "en_us"));
        }
        else{
            FileConfiguration serverLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us")));
            String onlyPlayersWarn = serverLanguageConfig.getString("ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            serverLanguageConfig.set("ONLY_PLAYERS_COMMAND", onlyPlayersWarn);

            plugin.saveLanguageConfig(serverLanguageConfig, config.getString("SETTINGS.serverLanguage", "en_us"));

            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
