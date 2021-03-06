package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandDelHome implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length == 0){
                delHome(player, "home");
                return true;
            }
            else if(arguments.length == 1){
                String homeName = arguments[0];
                delHome(player, homeName);
                return true;
            }
            else{
                player.sendMessage(command.getUsage());
                return true;
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public void delHome(Player player, String homeName){
        String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());

        if(sqLiteManager.haveHome(player.getUniqueId(), homeName)){
            sqLiteManager.removeHome(player.getUniqueId(), homeName);
            String homeRemovedMessage = localesManager.getMessage(playerLanguage, "HOME_REMOVED");
            player.sendMessage(formatString(homeRemovedMessage, homeName));
        }
        else{
            String homeNotFoundMessage = localesManager.getMessage(playerLanguage, "HOME_NOT_EXIST");
            player.sendMessage(formatString(homeNotFoundMessage, homeName));
        }
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home_name#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
