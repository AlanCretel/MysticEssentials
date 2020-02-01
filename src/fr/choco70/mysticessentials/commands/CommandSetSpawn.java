package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandSetSpawn implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            config.set("SPAWN.world", player.getWorld().getName());
            config.set("SPAWN.x", player.getLocation().getX());
            config.set("SPAWN.y", player.getLocation().getY());
            config.set("SPAWN.z", player.getLocation().getZ());
            config.set("SPAWN.pitch", player.getLocation().getPitch());
            config.set("SPAWN.yaw", player.getLocation().getYaw());
            try {
                config.save(plugin.getDataFolder().toString() + "/config.yml");
                String spawnSet = langsManager.getMessage(playerLanguage, "SPAWN_SET");
                player.sendMessage(spawnSet);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
