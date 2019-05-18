package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
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
    private langsManager langsManager = new langsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerUUID = player.getUniqueId().toString();

            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));
            String playerLanguage = playerConfig.getString("language", "en_us");

            if(playerConfig.isSet("last_location.world")){
                String teleportToLastLocationMessage = langsManager.getMessage(playerLanguage, "TELEPORT_LAST_LOCATION", "Teleporting to your last location...");

                player.sendMessage(teleportToLastLocationMessage);
                Location lastLocation = new Location(plugin.getServer().getWorld(playerConfig.get("last_location.world").toString()), playerConfig.getDouble("last_location.x"), playerConfig.getDouble("last_location.y"), playerConfig.getDouble("last_location.z"), Float.valueOf(playerConfig.getString("last_location.yaw","0")), Float.valueOf(playerConfig.getString("last_location.pitch","0")));
                player.teleport(lastLocation);
            }
            else{
                String noLastLocationMessage = langsManager.getMessage(playerLanguage, "NO_LAST_LOCATION", "You don't have any last location registered.");

                player.sendMessage(noLastLocationMessage);
            }
        }
        else{
            String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");

            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
