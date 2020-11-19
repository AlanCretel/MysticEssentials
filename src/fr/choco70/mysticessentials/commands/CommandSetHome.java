package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandSetHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            Integer homesLimit = playersManager.getHomesLimit(player);
            if((homesLimit > playersManager.getHomesNumber(player)) || player.hasPermission("mysticessentials.homeslimit.nolimit")){
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
                String homesLimitReached = localesManager.getMessage(playersManager.getPlayerLanguage(player), "HOMES_LIMIT_REACHED");
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
        playersManager.addHome(player, homeName);
        String homeSet = localesManager.getMessage(playersManager.getPlayerLanguage(player), "HOME_SET");
        player.sendMessage(formatString(homeSet, homeName));
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
