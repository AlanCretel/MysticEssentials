package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(arguments.length == 1){
            String targetName = arguments[0];
            Player target = sender.getServer().getPlayer(targetName);
            if(target != null){
                target.setFoodLevel(20);
                target.setSaturation(20);
                if(sender instanceof Player && ((Player)sender).hasPermission("mysticessentials.feed.others")){
                    Player player = (Player)sender;
                    String playerFed = localesManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_FED");
                    player.sendMessage(formatString(playerFed, null, target.getName()));
                    String targetFed = localesManager.getMessage(playersManager.getPlayerLanguage(target), "TARGET_FED");
                    target.sendMessage(formatString(targetFed, player, null));
                }
                else{
                    String playerFed = localesManager.getMessage(serverLanguage, "PLAYER_FED");
                    sender.sendMessage(formatString(playerFed, null, target.getName()));
                    String targetFed = localesManager.getMessage(playersManager.getPlayerLanguage(target), "TARGET_FED");
                    target.sendMessage(formatString(targetFed, null, null));
                }
            }
            else{
                if(sender instanceof Player){
                    Player player = (Player)sender;
                    String playerNotFound = localesManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, player, targetName));
                }
                else{
                    String playerNotFound = localesManager.getMessage(serverLanguage, "PLAYER_NOT_FOUND");
                    sender.sendMessage(formatString(playerNotFound, null, targetName));
                }
            }
        }
        else if(arguments.length == 0){
            if(sender instanceof Player){
                Player player = (Player)sender;
                player.setSaturation(20);
                player.setFoodLevel(20);
                String playerFed = localesManager.getMessage(serverLanguage, "PLAYER_FED_SELF");
                sender.sendMessage(formatString(playerFed, null, null));
            }
            else{
                String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
                sender.sendMessage(onlyPlayersWarn);
            }
        }
        return true;
    }

    public String formatString(String string, Player player, String target){
        String player_placeholder = "#player#";
        String target_placeholder = "#target#";
        if(target == null && player == null){
            return string.replaceAll(player_placeholder, "Server console");
        }
        else if(player == null){
            return string.replaceAll(target_placeholder, target);
        }
        else if(target == null){
            return string.replaceAll(player_placeholder, player.getName());
        }
        else{
            String formatedString = string.replaceAll(player_placeholder, player.getName());
            formatedString = formatedString.replaceAll(target_placeholder, target);
            return formatedString;
        }
    }

}