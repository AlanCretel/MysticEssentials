package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandSetDefaultHome implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

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
                    String noHomesMessage = langsManager.getMessage(playerLanguage, "NO_HOMES", "You don't have any home.");
                    player.sendMessage(noHomesMessage);
                }
                else{
                    if(homes.contains(arguments[0])){
                        playersManager.setDefaultHome(player, arguments[0]);
                        String defaultHomeSet = langsManager.getMessage(playerLanguage, "DEFAULT_HOME_SET", "Your default home is now: #home_name#.");
                        player.sendMessage(formatString(defaultHomeSet, arguments[0]));
                    }
                    else{
                        String homeNotFoundMessage = langsManager.getMessage(playerLanguage, "HOME_NOT_EXIST", "You don't have any home called #home_name#.");
                        player.sendMessage(formatString(homeNotFoundMessage, arguments[0]));
                    }
                }
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home_name#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
