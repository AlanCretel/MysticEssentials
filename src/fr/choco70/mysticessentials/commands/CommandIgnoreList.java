package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandIgnoreList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLocale = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(sqLiteManager.getIgnoredPlayers(player.getUniqueId()) != null){
                String ignoredList = localesManager.getMessage(playerLocale, "IGNORED_LIST");
                player.sendMessage(formatString(ignoredList, playersManager.toUserNames(sqLiteManager.getIgnoredPlayers(player.getUniqueId()))));
            }
            else{
                String noIgnoredPlayers = localesManager.getMessage(playerLocale, "NO_IGNORED_PLAYERS");
                player.sendMessage(noIgnoredPlayers);
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
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
