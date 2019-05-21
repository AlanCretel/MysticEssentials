package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandDelHome implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private playersManager playersManager = new playersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 0 && arguments.length == 1){
                String homeName = arguments[0];
                delHome(player, homeName);
                return true;
            }
            else if(arguments.length == 0){
                delHome(player, "home");
                return true;
            }
            else{
                player.sendMessage(command.getUsage());
                return true;
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public void delHome(Player player, String homeName){
        FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
        String playerLanguage = playersManager.getPlayerLanguage(player);

        if(playerConfig.isConfigurationSection("homes." + homeName)){
            playerConfig.set("homes." + homeName, null);
            playersManager.savePlayerConfig(player, playerConfig);
            String homeRemovedMessage = langsManager.getMessage(playerLanguage, "HOME_REMOVED", "Home #home_name# successfully removed.");
            player.sendMessage(formatString(homeRemovedMessage, homeName));
        }
        else{
            String homeNotFoundMessage = langsManager.getMessage(playerLanguage, "HOME_NOT_EXIST", "You don't have any home called #home_name#.");
            player.sendMessage(formatString(homeNotFoundMessage, homeName));
        }
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home_name#";
        return string.replaceAll(homeName_placeholder, homeName);
    }
}
