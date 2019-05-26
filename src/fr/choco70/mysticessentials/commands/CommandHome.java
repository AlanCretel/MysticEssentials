package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage");
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(arguments.length == 0){
                if(playersManager.getHomeList(player) != null && playersManager.getHomeList(player).size() != 0){
                    Set<String> homes = playersManager.getHomeList(player);
                    if(homes.contains("home") || playersManager.getDefaultHome(player) != null){
                        if(playersManager.getDefaultHome(player) != null){
                            String homeName = playersManager.getDefaultHome(player);
                            toHome(player, homeName, playerConfig, playerLanguage);
                        }
                        else{
                            toHome(player, "home", playerConfig, playerLanguage);
                        }
                    }
                    else{
                        String homeListMessage = langsManager.getMessage(playerLanguage, "HOME_LIST", "Your homes: #home_list#.");
                        player.sendMessage(formatString(homeListMessage, null, homes.toString()));
                    }
                }
                else{
                    String noHomesMessage = langsManager.getMessage(playerLanguage, "NO_HOMES", "You don't have any home.");
                    player.sendMessage(formatString(noHomesMessage, null, playersManager.getHomeList(player).toString()));
                }
            }
            else{
                if(arguments.length == 1 && playersManager.getHomeList(player) != null){
                    String homeName = arguments[0];
                    if(playerConfig.isConfigurationSection("homes." + homeName)){
                        toHome(player, homeName, playerConfig, playerLanguage);
                    }
                    else{
                        String homeNotFoundMessage = langsManager.getMessage(playerLanguage, "HOME_NOT_EXIST", "You don't have any home called #home_name#.");
                        player.sendMessage(formatString(homeNotFoundMessage, homeName, playersManager.getHomeList(player).toString()));
                    }
                }
                else if(playersManager.getHomeList(player) == null){
                    String homeNotFoundMessage = langsManager.getMessage(playerLanguage, "HOME_NOT_EXIST", "You don't have any home called #home_name#.");
                    player.sendMessage(formatString(homeNotFoundMessage, arguments[0], null));
                }
                else{
                    player.sendMessage(command.getUsage());
                }
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    private void toHome(Player player, String homeName, FileConfiguration playerConfig, String playerLanguage){
        String teleportToHome = langsManager.getMessage(playerLanguage, "TELEPORT_TO_HOME", "Teleportation to your home #home_name#.");
        player.sendMessage(formatString(teleportToHome, homeName, null));
        Location homeLocation = player.getLocation().clone();
        homeLocation.setWorld(player.getServer().getWorld(playerConfig.get("homes." + homeName + ".world").toString()));
        homeLocation.setX(Double.valueOf(playerConfig.get("homes." + homeName + ".x").toString()));
        homeLocation.setY(Double.valueOf(playerConfig.get("homes." + homeName + ".y").toString()));
        homeLocation.setZ(Double.valueOf(playerConfig.get("homes." + homeName + ".z").toString()));
        homeLocation.setPitch(Float.valueOf(playerConfig.get("homes." + homeName + ".pitch").toString()));
        homeLocation.setYaw(Float.valueOf(playerConfig.get("homes." + homeName + ".yaw").toString()));
        playersManager.setLastLocation(player);
        player.teleport(homeLocation);
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
