package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private fr.choco70.mysticessentials.utils.langsManager langsManager = new langsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerUUID = player.getUniqueId().toString();

            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));
            Set<String> homes = playerConfig.getConfigurationSection("homes").getKeys(false);
            String playerLanguage = playerConfig.getString("language", serverLanguage);

            if(arguments.length != 0){
                if(arguments.length == 1){
                    String homeName = arguments[0];
                    if(playerConfig.isConfigurationSection("homes." + homeName)){
                        toHome(player, homeName, playerConfig, playerLanguage);
                    }
                    else{
                        String homeNotFoundMessage = langsManager.getMessage(playerLanguage, "HOME_NOT_EXIST", "You don't have any home called #home_name#.");
                        player.sendMessage(formatString(homeNotFoundMessage, homeName, homes.toString()));
                    }
                }
                else{
                    player.sendMessage(command.getUsage());
                }
            }
            else{
                if(homes != null && homes.size() != 0){
                    if(homes.contains("home")){
                        toHome(player, "home", playerConfig, playerLanguage);
                    }
                    else{
                        String homeListMessage = langsManager.getMessage(playerLanguage, "HOME_LIST", "Your homes: #home_list#.");
                        player.sendMessage(formatString(homeListMessage, null, homes.toString()));
                    }
                }
                else{
                    String noHomesMessage = langsManager.getMessage(playerLanguage, "NO_HOMES", "You don't have any home.");
                    player.sendMessage(formatString(noHomesMessage, null, homes.toString()));
                }
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");

            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public void toHome(Player player, String homeName, FileConfiguration playerConfig, String playerLanguage){
        String teleportToHome = langsManager.getMessage(playerLanguage, "TELEPORT_TO_HOME", "Teleportation to your home #home_name#.");
        player.sendMessage(formatString(teleportToHome, homeName, null));

        Double x = Double.valueOf(playerConfig.get("homes." + homeName + ".x").toString());
        Double y = Double.valueOf(playerConfig.get("homes." + homeName + ".y").toString());
        Double z = Double.valueOf(playerConfig.get("homes." + homeName + ".z").toString());
        Float pitch = Float.valueOf(playerConfig.get("homes." + homeName + ".pitch").toString());
        Float yaw = Float.valueOf(playerConfig.get("homes." + homeName + ".yaw").toString());
        World world = player.getServer().getWorld(playerConfig.get("homes." + homeName + ".world").toString());

        Location homeLocation = player.getLocation().clone();

        homeLocation.setWorld(world);
        homeLocation.setX(x);
        homeLocation.setY(y);
        homeLocation.setZ(z);
        homeLocation.setPitch(pitch);
        homeLocation.setYaw(yaw);

        player.teleport(homeLocation);
    }

    public String formatString(String string, String homeName, String homeList){
        String homeName_placeholder = "#home_name#";
        String homeList_placeholder = "#home_list#";

        if(homeList == null){
            String formatedString = string.replaceAll(homeName_placeholder, homeName);
            return formatedString;
        }
        else if(homeName == null){
            String formatedString = string.replaceAll(homeList_placeholder, homeList);
            return formatedString;
        }
        else{
            String formatedString = string.replaceAll(homeName_placeholder, homeName);
            formatedString = formatedString.replaceAll(homeList_placeholder, homeList);
            return formatedString;
        }
    }
}
