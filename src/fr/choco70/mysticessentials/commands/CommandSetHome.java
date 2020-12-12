package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetHome implements CommandExecutor {

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final PlayersManager playersManager = plugin.getPlayersManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            Integer homesLimit = playersManager.getHomesLimit(player);
            if((homesLimit > sqLiteManager.getHomes(player.getUniqueId()).size()) || player.hasPermission("mysticessentials.homeslimit.nolimit")){
                if(arguments.length != 1 && arguments.length != 0){
                    player.sendMessage(command.getUsage());
                }
                else if(arguments.length == 0){
                    addHome(player, "home");
                }
                else{
                    String homeName = arguments[0];
                    addHome(player, homeName);
                }
            }
            else{
                String homesLimitReached = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "HOMES_LIMIT_REACHED");
                player.sendMessage(homesLimitReached.replaceAll("#homes_limit#", homesLimit.toString()));
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public void addHome(Player player, String homeName){
        if(!sqLiteManager.homeExist(player.getUniqueId(), homeName)){
            sqLiteManager.insertHome(player.getUniqueId(), homeName, false, player.getLocation());
        }
        else{
            sqLiteManager.updateHome(player.getUniqueId(), homeName, false, player.getLocation());
        }
        String homeSet = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "HOME_SET");
        player.sendMessage(formatString(homeSet, homeName));
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
