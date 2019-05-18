package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandDelSpawn implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(config.isConfigurationSection("SPAWN")){
            config.set("SPAWN", null);
            if(sender instanceof Player){
                Player player = (Player)sender;
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
                String playerLanguage = playerConfig.getString("language", serverLanguage);
                player.sendMessage(langsManager.getMessage(playerLanguage, "SPAWN_REMOVED", "Successfully removed spawn location."));
            }
            else{

            }
            sender.sendMessage(langsManager.getMessage(serverLanguage, "SPAWN_REMOVED", "Successfully removed spawn location."));
        }
        else{
            if(sender instanceof Player){
                Player player = (Player)sender;
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
                String playerLanguage = playerConfig.getString("language", serverLanguage);
                player.sendMessage(langsManager.getMessage(playerLanguage, "NO_SPAWN", "No spawn set."));
            }
            else{
                sender.sendMessage(langsManager.getMessage(serverLanguage, "NO_SPAWN", "No spawn set."));
            }
        }
        return true;
    }
}
