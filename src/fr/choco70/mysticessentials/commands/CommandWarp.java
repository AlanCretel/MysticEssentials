package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandWarp implements CommandExecutor{

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
            if(arguments.length == 1){
                String warpName = arguments[0];
                if(config.isConfigurationSection("WARPS." + warpName) && (player.hasPermission("mysticessentials.warps." + warpName) || !config.getBoolean("SETTINGS.perWarpPermission", false)) && !warpName.equalsIgnoreCase("list")){
                    Location warpLocation = player.getLocation().clone();
                    Double x = Double.valueOf(config.get("WARPS." + warpName + ".x").toString());
                    Double y = Double.valueOf(config.get("WARPS." + warpName + ".y").toString());
                    Double z = Double.valueOf(config.get("WARPS." + warpName + ".z").toString());
                    Float pitch = Float.valueOf(config.get("WARPS." + warpName + ".pitch").toString());
                    Float yaw = Float.valueOf(config.get("WARPS." + warpName + ".yaw").toString());
                    World world = player.getServer().getWorld(config.get("WARPS." + warpName + ".world").toString());
                    warpLocation.setWorld(world);
                    warpLocation.setPitch(pitch);
                    warpLocation.setYaw(yaw);
                    warpLocation.setX(x);
                    warpLocation.setY(y);
                    warpLocation.setZ(z);

                    playersManager.setLastLocation(player);

                    String teleportedToWarp = langsManager.getMessage(playerLanguage, "TELEPORTED_TO_WARP", "Successfully teleported to warp #warp#");
                    player.sendMessage(formatString(teleportedToWarp, warpName));
                    player.teleport(warpLocation);
                }
                else if(config.isConfigurationSection("WARPS." + warpName) && !player.hasPermission("mysticessentials.warps." + warpName) && config.getBoolean("SETTINGS.perWarpPermission", false)){
                    String noWarpPermission = langsManager.getMessage(playerLanguage, "NO_WARP_PERMISSION", "You don't have permission to teleport to this warp. (#permission#)");
                    player.sendMessage(formatString(noWarpPermission, warpName));
                }
                else if(warpName.equalsIgnoreCase("list") && player.hasPermission("mysticessentials.warplist")){
                    Set<String> warps = config.getConfigurationSection("WARPS.").getKeys(false);
                    if(warps.size() == 0){
                        String noWarps = langsManager.getMessage(playerLanguage, "NO_WARPS", "No warps were set.");
                        player.sendMessage(noWarps);
                    }
                    else{
                        String warpListMessage = langsManager.getMessage(playerLanguage, "WARP_LIST", "Available warps: #warp_list#.");
                        player.sendMessage(formatString(warpListMessage, warps));
                    }
                }
                else{
                    String noWarpFound = langsManager.getMessage(playerLanguage, "WARP_NOT_EXIST", "The warp #warp# does not exist.");
                    player.sendMessage(formatString(noWarpFound, warpName));
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
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
    public String formatString(String string, Set<String> warps){
        String warpList_placeholder = "#warp_list#";
        return string.replaceAll(warpList_placeholder, warps.toString());
    }
}
