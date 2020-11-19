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

public class CommandHomeList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            Set<String> homes = playersManager.getHomeList(player);

            if(homes != null && homes.size() == 0){
                String noHomesMessage = localesManager.getMessage(playerLanguage, "NO_HOMES");
                player.sendMessage(formatString(noHomesMessage, ""));
            }
            if(homes == null){
                String noHomesMessage = localesManager.getMessage(playerLanguage, "NO_HOMES");
                player.sendMessage(formatString(noHomesMessage, ""));
            }
            else{
                String homeListMessage = localesManager.getMessage(playerLanguage, "HOME_LIST");
                player.sendMessage(formatString(homeListMessage, homes.toString()));
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String homes){
        String homeList_placeholder = "#home_list#";
        return string.replaceAll(homeList_placeholder, homes);
    }
}
