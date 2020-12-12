package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpDeny implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            String teleportationDeniedSender = localesManager.getMessage(playerLanguage, "TELEPORTATION_DENIED_SENDER");
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                String requesterLanguage = sqLiteManager.getPlayerLocale(requester.getUniqueId());
                String teleportationDeniedRequester = localesManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpa().remove(player);
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                String requesterLanguage = sqLiteManager.getPlayerLocale(requester.getUniqueId());
                String teleportationDeniedRequester = localesManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpahere().remove(player);
            }
            else{
                String noTeleportationRequest = localesManager.getMessage(playerLanguage, "NO_TELEPORTATION_REQUEST");
                player.sendMessage(noTeleportationRequest);
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Player target){
        String target_placeholder = "#target#";
        return string.replaceAll(target_placeholder, target.getName());
    }
}
