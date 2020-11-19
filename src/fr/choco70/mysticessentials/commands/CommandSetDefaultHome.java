package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandSetDefaultHome implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(arguments.length == 0){
                player.sendMessage(command.getUsage());
            }
            else{
                String homeName = arguments[0];
                Set<String> homes = playersManager.getHomeList(player);
                if(playersManager.getHomeList(player) == null){
                    String noHomesMessage = localesManager.getMessage(playerLanguage, "NO_HOMES");
                    player.sendMessage(noHomesMessage);
                }
                else{
                    if(homes.contains(homeName)){
                        playersManager.setDefaultHome(player, homeName);
                        String defaultHomeSet = localesManager.getMessage(playerLanguage, "DEFAULT_HOME_SET");
                        player.sendMessage(formatString(defaultHomeSet, homeName));
                    }
                    else{
                        String homeNotFoundMessage = localesManager.getMessage(playerLanguage, "HOME_NOT_EXIST");
                        player.sendMessage(formatString(homeNotFoundMessage, homeName));
                    }
                }
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home_name#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
