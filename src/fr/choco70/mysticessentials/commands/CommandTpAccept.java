package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpAccept implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                if(requester != null && requester.isOnline()){
                    requester.teleport(player.getLocation());
                    if(sqLiteManager.haveLastLocation(requester.getUniqueId())){
                        sqLiteManager.updateLastLocation(requester.getUniqueId(), requester.getLocation());
                    }
                    else{
                        sqLiteManager.setLastLocation(requester.getUniqueId(), requester.getLocation());
                    }
                    plugin.getTpa().remove(player);
                    String requesterLanguage = sqLiteManager.getPlayerLocale(requester.getUniqueId());
                    String teleportedTo = localesManager.getMessage(requesterLanguage, "TPA_TELEPORTED");
                    requester.sendMessage(formatString(teleportedTo, player.getName(), requester));
                }
                else{
                    String playerOffline = localesManager.getMessage(playerLanguage, "PLAYER_OFFLINE");
                    player.sendMessage(formatString(playerOffline, arguments[0], player));
                    plugin.getTpa().remove(player);
                }
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                if(requester != null && requester.isOnline()){
                    player.teleport(requester.getLocation());
                    if(sqLiteManager.haveLastLocation(player.getUniqueId())){
                        sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
                    }
                    else{
                        sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
                    }
                    plugin.getTpahere().remove(player);

                    String requesterLanguage = sqLiteManager.getPlayerLocale(requester.getUniqueId());
                    String teleportedPlayer = localesManager.getMessage(requesterLanguage, "TPAHERE_TELEPORTED");
                    requester.sendMessage(formatString(teleportedPlayer, player.getName(), requester));
                }
                else{
                    String playerOffline = localesManager.getMessage(playerLanguage, "PLAYER_OFFLINE");
                    player.sendMessage(formatString(playerOffline, arguments[0], player));
                    plugin.getTpahere().remove(player);
                }
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

    public String formatString(String string, String target, Player player){
        String target_placeholder = "#target#";
        String requester_placeholder = "#requester#";
        String formatedString = string.replaceAll(target_placeholder, target);
        formatedString = formatedString.replaceAll(requester_placeholder, player.getName());
        return formatedString;
    }
}
