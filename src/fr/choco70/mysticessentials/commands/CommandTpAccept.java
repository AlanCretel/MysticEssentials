package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandTpAccept implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private playersManager playersManager = new playersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                if(requester != null && requester.isOnline()){
                    requester.teleport(player.getLocation());
                    plugin.getTpa().remove(player);
                    String requesterLanguage = playersManager.getPlayerLanguage(requester);
                    String teleportedTo = langsManager.getMessage(requesterLanguage, "TPA_TELEPORTED", "Successfully teleported to #target#.");
                    requester.sendMessage(formatString(teleportedTo, player.getName(), requester));
                }
                else{
                    String playerOffline = langsManager.getMessage(playerLanguage, "PLAYER_OFFLINE", "Player #target# is offline.");
                    player.sendMessage(formatString(playerOffline, arguments[0], player));
                    plugin.getTpa().remove(player);
                }
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                if(requester != null && requester.isOnline()){
                    player.teleport(requester.getLocation());
                    plugin.getTpahere().remove(player);

                    String requesterLanguage = playersManager.getPlayerLanguage(requester);
                    String teleportedPlayer = langsManager.getMessage(requesterLanguage, "TPAHERE_TELEPORTED", "Successfully teleported #target# to you.");
                    requester.sendMessage(formatString(teleportedPlayer, player.getName(), requester));
                }
                else{
                    String playerOffline = langsManager.getMessage(playerLanguage, "PLAYER_OFFLINE", "Player #target# is offline.");
                    player.sendMessage(formatString(playerOffline, arguments[0], player));
                    plugin.getTpahere().remove(player);
                }
            }
            else{
                String noTeleportationRequest = langsManager.getMessage(playerLanguage, "NO_TELEPORTATION_REQUEST", "You don't have any teleportation request.");
                player.sendMessage(noTeleportationRequest);
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String target, Player player){
        String target_placeholder = "#target#";
        String requester_placeholder = "#requester#";
        String formatedString = string.replaceAll(target_placeholder, target);
        formatedString = formatedString.replaceAll(requester_placeholder, player.getName());
        return formatedString;
    }
}
