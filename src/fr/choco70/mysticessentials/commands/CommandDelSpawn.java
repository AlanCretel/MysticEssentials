package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandDelSpawn implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(config.isConfigurationSection("SPAWN")){
            config.set("SPAWN", null);
            if(sender instanceof Player){
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                player.sendMessage(langsManager.getMessage(playerLanguage, "SPAWN_REMOVED"));
            }
            else{
                sender.sendMessage(langsManager.getMessage(serverLanguage, "SPAWN_REMOVED"));
            }
        }
        else{
            if(sender instanceof Player){
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                player.sendMessage(langsManager.getMessage(playerLanguage, "NO_SPAWN"));
            }
            else{
                sender.sendMessage(langsManager.getMessage(serverLanguage, "NO_SPAWN"));
            }
        }
        return true;
    }
}
