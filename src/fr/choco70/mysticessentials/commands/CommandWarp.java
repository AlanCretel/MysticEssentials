package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandWarp implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(arguments.length == 1){
                String warpName = arguments[0].toLowerCase();
                if(sqLiteManager.warpExist(warpName) && (player.hasPermission("mysticessentials.warps." + warpName) || !config.getBoolean("SETTINGS.perWarpPermission", false)) && !warpName.equalsIgnoreCase("list")){
                    Location warpLocation = sqLiteManager.getWarpLocation(warpName);

                    if(sqLiteManager.haveLastLocation(player.getUniqueId())){
                        sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
                    }
                    else{
                        sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
                    }

                    String teleportedToWarp = localesManager.getMessage(playerLanguage, "TELEPORTED_TO_WARP");
                    player.sendMessage(formatString(teleportedToWarp, warpName));
                    player.teleport(warpLocation);
                }
                else if(sqLiteManager.warpExist(warpName) && !player.hasPermission("mysticessentials.warps." + warpName) && config.getBoolean("SETTINGS.perWarpPermission", false)){
                    String noWarpPermission = localesManager.getMessage(playerLanguage, "NO_WARP_PERMISSION");
                    player.sendMessage(formatString(noWarpPermission, warpName));
                }
                else if(warpName.equalsIgnoreCase("list") && player.hasPermission("mysticessentials.warplist")){
                    ArrayList<String> warps = sqLiteManager.getWarps();
                    if(warps.size() == 0){
                        String noWarps = localesManager.getMessage(playerLanguage, "NO_WARPS");
                        player.sendMessage(noWarps);
                    }
                    else{
                        String warpListMessage = localesManager.getMessage(playerLanguage, "WARP_LIST");
                        player.sendMessage(formatString(warpListMessage, warps));
                    }
                }
                else{
                    String noWarpFound = localesManager.getMessage(playerLanguage, "WARP_NOT_EXIST");
                    player.sendMessage(formatString(noWarpFound, warpName));
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String warp){
        String warp_placeholder = "#warp#";
        String permission_placeholder = "#permission#";
        String formatedString = string.replaceAll(warp_placeholder, warp);
        formatedString = formatedString.replaceAll(permission_placeholder, "mysticessentials.warps." + warp);
        return formatedString;
    }
    public String formatString(String string, ArrayList<String> warps){
        String warpList_placeholder = "#warp_list#";
        return string.replaceAll(warpList_placeholder, warps.toString());
    }
}
