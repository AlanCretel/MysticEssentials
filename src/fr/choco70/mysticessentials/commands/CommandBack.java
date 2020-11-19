package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
            String playerLanguage = playersManager.getPlayerLanguage(player);

            if(playerConfig.isSet("last_location.world")){
                String teleportToLastLocationMessage = localesManager.getMessage(playerLanguage, "TELEPORT_LAST_LOCATION");

                player.sendMessage(teleportToLastLocationMessage);
                Location lastLocation = new Location(plugin.getServer().getWorld(playerConfig.get("last_location.world").toString()), playerConfig.getDouble("last_location.x"), playerConfig.getDouble("last_location.y"), playerConfig.getDouble("last_location.z"), Float.valueOf(playerConfig.getString("last_location.yaw","0")), Float.valueOf(playerConfig.getString("last_location.pitch","0")));
                player.teleport(lastLocation);
                playersManager.setLastLocation(player);
            }
            else{
                String noLastLocationMessage = localesManager.getMessage(playerLanguage, "NO_LAST_LOCATION");
                player.sendMessage(noLastLocationMessage);
            }
        }
        else{
            String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
