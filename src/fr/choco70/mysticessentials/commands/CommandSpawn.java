package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private playersManager playersManager = new playersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);

            if(config.isSet("SPAWN.x") && config.isSet("SPAWN.y") && config.isSet("SPAWN.z") && config.isSet("SPAWN.pitch") && config.isSet("SPAWN.yaw") && config.isSet("SPAWN.world")){
                String teleportedToSpawn = langsManager.getMessage(playerLanguage, "TELEPORT_TO_SPAWN", "Teleported to spawn.");
                playersManager.setLastLocation(player);
                plugin.toSpawn(player, teleportedToSpawn);
            }
            else{
                player.sendMessage(langsManager.getMessage(playerLanguage, "NO_SPAWN", "No spawn set."));
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
