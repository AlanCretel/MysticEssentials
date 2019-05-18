package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandSetSpawn implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", serverLanguage);
            config.set("SPAWN.world", player.getWorld().getName());
            config.set("SPAWN.x", player.getLocation().getX());
            config.set("SPAWN.y", player.getLocation().getY());
            config.set("SPAWN.z", player.getLocation().getZ());
            config.set("SPAWN.pitch", player.getLocation().getPitch());
            config.set("SPAWN.yaw", player.getLocation().getYaw());
            try {
                config.save(plugin.getDataFolder().toString() + "/config.yml");
                String spawnSet = langsManager.getMessage(playerLanguage, "SPAWN_SET", "Successfully set spawn location.");
                player.sendMessage(spawnSet);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
