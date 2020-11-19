package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandDelSpawn implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(config.isConfigurationSection("SPAWN")){
            config.set("SPAWN", null);
            if(sender instanceof Player){
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                player.sendMessage(localesManager.getMessage(playerLanguage, "SPAWN_REMOVED"));
            }
            else{
                sender.sendMessage(localesManager.getMessage(serverLanguage, "SPAWN_REMOVED"));
            }
        }
        else{
            if(sender instanceof Player){
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                player.sendMessage(localesManager.getMessage(playerLanguage, "NO_SPAWN"));
            }
            else{
                sender.sendMessage(localesManager.getMessage(serverLanguage, "NO_SPAWN"));
            }
        }
        return true;
    }
}
