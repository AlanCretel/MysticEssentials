package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandIgnoreList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(playersManager.getIgnoredPlayers(player) != null){
                String ignoredList = langsManager.getMessage(playersManager.getPlayerLanguage(player), "IGNORED_LIST", "Players you ignore: /n#ignored_list#");
                player.sendMessage(formatString(ignoredList, playersManager.toUserName(playersManager.getPlayersIgnored(player))));
            }
            else{
                String noIgnoredPlayers = langsManager.getMessage(playersManager.getPlayerLanguage(player), "NO_IGNORED_PLAYERS", "You don't ignore anyone.");
                player.sendMessage(noIgnoredPlayers);
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, List<String> ignoredPlayers){
        String ignoredList_placeholder = "#ignored_list#";
        String newLine_placeholder = "/n";
        String formatedString = string.replaceAll(newLine_placeholder, "\n");
        return formatedString.replaceAll(ignoredList_placeholder, ignoredPlayers.toString());
    }
}
