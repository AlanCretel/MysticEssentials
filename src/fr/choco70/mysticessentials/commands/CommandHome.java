package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

public class CommandHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(arguments.length == 0){
                if(sqLiteManager.getHomes(player.getUniqueId()) != null && sqLiteManager.getHomes(player.getUniqueId()).size() != 0){
                    ArrayList<String> homes = sqLiteManager.getHomes(player.getUniqueId());
                    if(homes.contains("home") || sqLiteManager.getDefaultHomeName(player.getUniqueId()) != null){
                        if(sqLiteManager.getDefaultHomeName(player.getUniqueId()) != null){
                            toHome(player, sqLiteManager.getDefaultHomeName(player.getUniqueId()), playerLanguage);
                        }
                        else{
                            toHome(player, "home", playerLanguage);
                        }
                    }
                    else{
                        String homeListMessage = localesManager.getMessage(playerLanguage, "HOME_LIST");
                        player.sendMessage(formatString(homeListMessage, null, homes.toString()));
                    }
                }
                else{
                    String noHomesMessage = localesManager.getMessage(playerLanguage, "NO_HOMES");
                    player.sendMessage(formatString(noHomesMessage, null, null));
                }
            }
            else{
                if(arguments.length == 1 && sqLiteManager.getHomes(player.getUniqueId()) != null){
                    String homeName = arguments[0];
                    if(sqLiteManager.haveHome(player.getUniqueId(), homeName)){
                        toHome(player, homeName, playerLanguage);
                    }
                    else{
                        String homeNotFoundMessage = localesManager.getMessage(playerLanguage, "HOME_NOT_EXIST");
                        player.sendMessage(formatString(homeNotFoundMessage, homeName, sqLiteManager.getHomes(player.getUniqueId()).toString()));
                    }
                }
                else if(sqLiteManager.getHomes(player.getUniqueId()) == null){
                    String homeNotFoundMessage = localesManager.getMessage(playerLanguage, "HOME_NOT_EXIST");
                    player.sendMessage(formatString(homeNotFoundMessage, arguments[0], null));
                }
                else{
                    player.sendMessage(command.getUsage());
                }
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    private void toHome(Player player, String homeName, String playerLanguage){
        String teleportToHome = localesManager.getMessage(playerLanguage, "TELEPORT_TO_HOME");
        player.sendMessage(formatString(teleportToHome, homeName, null));
        Location homeLocation = sqLiteManager.getHomeLocation(player.getUniqueId(), homeName);
        if(sqLiteManager.haveLastLocation(player.getUniqueId())){
            sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
        }
        else{
            sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
        }
        if(player.isInsideVehicle()){
            if(player.getVehicle() instanceof Horse){
                Horse horse = (Horse) player.getVehicle();
                horse.teleport(homeLocation);
                player.teleport(horse, PlayerTeleportEvent.TeleportCause.UNKNOWN);
            }
            else{
                player.teleport(homeLocation);
            }
        }
        else{
            player.teleport(homeLocation);
        }
    }

    public String formatString(String string, String homeName, String homeList){
        String homeName_placeholder = "#home_name#";
        String homeList_placeholder = "#home_list#";
        if(homeList == null && homeName == null){
            return string;
        }
        else if(homeList == null){
            return string.replaceAll(homeName_placeholder, homeName);
        }
        else if(homeName == null){
            return string.replaceAll(homeList_placeholder, homeList);
        }
        else{
            String formatedString = string.replaceAll(homeName_placeholder, homeName);
            formatedString = formatedString.replaceAll(homeList_placeholder, homeList);
            return formatedString;
        }
    }
}
