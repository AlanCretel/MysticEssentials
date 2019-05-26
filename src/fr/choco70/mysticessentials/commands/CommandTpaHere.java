package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandTpaHere implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
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
                    if(!plugin.getTpahere().containsKey(player)){
                        String tpaRequestTarget = langsManager.getMessage(targetLanguage, "TPAHERE_REQUEST_TARGET", "#requester# requested you to teleport to them./n Type /tpaccept to accept them./n Type /tpdeny to deny them.");
                        target.sendMessage(formatString(tpaRequestTarget, player, targetName));

                        String tpaRequestSender = langsManager.getMessage(playerLanguage, "TPAHERE_REQUEST_SENDER", "Sent a teleportation request to #target#.");
                        player.sendMessage(formatString(tpaRequestSender, player, targetName));
                        plugin.getTpahere().put(target, player);
                    }
                    else{
                        String requestAlreadySent = langsManager.getMessage(playerLanguage, "REQUEST_ALREADY_SENT", "You already sent a teleportation request to #target#.");
                        player.sendMessage(formatString(requestAlreadySent, player, targetName));
                    }
                }
                else if(player == target){
                    String selfRequested = langsManager.getMessage(playerLanguage, "SELF_REQUESTED", "You can't send a request to yourself.");
                    player.sendMessage(formatString(selfRequested, player, player.getName()));
                }
                else if(target == null){
                    String playerNotFound = langsManager.getMessage(playerLanguage, "PLAYER_NOT_FOUND", "Player #target# was not found.");
                    player.sendMessage(formatString(playerNotFound, player, arguments[0]));
                }
                else{
                    String playerOffline = langsManager.getMessage(playerLanguage, "PLAYER_OFFLINE", "Player #target# is offline.");
                    player.sendMessage(formatString(playerOffline, player, arguments[0]));
                }
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
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
