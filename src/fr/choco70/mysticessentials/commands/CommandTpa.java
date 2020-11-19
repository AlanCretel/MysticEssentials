package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.DelaysManager;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandTpa implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private DelaysManager delaysManager = plugin.getDelaysManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(arguments.length != 1){
                player.sendMessage(command.getUsage());
            }
            else if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null && target.isOnline() && target != player){
                    String targetLanguage = playersManager.getPlayerLanguage(target);
                    String targetName = target.getName();
                    if(!plugin.getTpa().containsKey(player)){
                        String tpaRequestTarget = localesManager.getMessage(targetLanguage, "TPA_REQUEST_TARGET");
                        target.sendMessage(formatString(tpaRequestTarget, player, targetName));

                        String tpaRequestSender = localesManager.getMessage(playerLanguage, "TPA_REQUEST_SENDER");
                        player.sendMessage(formatString(tpaRequestSender, player, targetName));
                        plugin.getTpa().put(target, player);
                    }
                    else{
                        String requestAlreadySent = localesManager.getMessage(playerLanguage, "REQUEST_ALREADY_SENT");
                        player.sendMessage(formatString(requestAlreadySent, player, targetName));
                    }
                }
                else if(player == target){
                    String selfRequested = localesManager.getMessage(playerLanguage, "SELF_REQUESTED");
                    player.sendMessage(formatString(selfRequested, player, player.getName()));
                }
                else if(target == null){
                    String playerNotFound = localesManager.getMessage(playerLanguage, "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, player, arguments[0]));
                }
                else{
                    String playerOffline = localesManager.getMessage(playerLanguage, "PLAYER_OFFLINE");
                    player.sendMessage(formatString(playerOffline, player, arguments[0]));
                }
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Player player, String targetName){
        String target_placeholder = "#target#";
        String sender_placeholder = "#sender#";
        String newLine_placeholder = "/n";

        String formatedString = string.replaceAll(target_placeholder, targetName);
        formatedString = formatedString.replaceAll(sender_placeholder, player.getName());
        formatedString = formatedString.replaceAll(newLine_placeholder, "\n");

        return formatedString;
    }

}
