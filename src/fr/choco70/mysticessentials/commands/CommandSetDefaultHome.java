package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandSetDefaultHome implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(arguments.length == 0){
                player.sendMessage(command.getUsage());
            }
            else{
                String homeName = arguments[0];
                ArrayList<String> homes = sqLiteManager.getHomes(player.getUniqueId());
                if(homes == null){
                    String noHomesMessage = localesManager.getMessage(playerLanguage, "NO_HOMES");
                    player.sendMessage(noHomesMessage);
                }
                else{
                    if(homes.contains(homeName)){
                        sqLiteManager.setDefaultHome(player.getUniqueId(), homeName);
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
