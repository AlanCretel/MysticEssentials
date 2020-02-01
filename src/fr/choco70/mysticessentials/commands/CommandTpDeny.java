package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpDeny implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = langsManager.getServerLanguage();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            String teleportationDeniedSender = langsManager.getMessage(playerLanguage, "TELEPORTATION_DENIED_SENDER");
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                String requesterLanguage = playersManager.getPlayerLanguage(requester);
                String teleportationDeniedRequester = langsManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpa().remove(player);
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                String requesterLanguage = playersManager.getPlayerLanguage(requester);
                String teleportationDeniedRequester = langsManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpahere().remove(player);
            }
            else{
                String noTeleportationRequest = langsManager.getMessage(playerLanguage, "NO_TELEPORTATION_REQUEST");
                player.sendMessage(noTeleportationRequest);
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Player target){
        String target_placeholder = "#target#";
        return string.replaceAll(target_placeholder, target.getName());
    }
}
