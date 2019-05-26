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

public class CommandHomeList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            Set<String> homes = playersManager.getHomeList(player);

            if(homes != null && homes.size() == 0){
                String noHomesMessage = langsManager.getMessage(playerLanguage, "NO_HOMES", "You don't have any home.");
                player.sendMessage(formatString(noHomesMessage, homes.toString()));
            }
            else{
                String homeListMessage = langsManager.getMessage(playerLanguage, "HOME_LIST", "Your homes: #home_list#.");
                player.sendMessage(formatString(homeListMessage, homes.toString()));
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String homes){
        String homeList_placeholder = "#home_list#";
        return string.replaceAll(homeList_placeholder, homes);
    }
}
