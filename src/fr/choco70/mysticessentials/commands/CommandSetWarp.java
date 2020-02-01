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

import java.io.File;
import java.io.IOException;

public class CommandSetWarp implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        String serverLanguage = langsManager.getServerLanguage();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(arguments.length == 1){
                String warpName = arguments[0];
                Location playerLocation = player.getLocation();
                config.set("WARPS." + warpName + ".world", playerLocation.getWorld().getName());
                config.set("WARPS." + warpName + ".x", playerLocation.getX());
                config.set("WARPS." + warpName + ".y", playerLocation.getY());
                config.set("WARPS." + warpName + ".z", playerLocation.getZ());
                config.set("WARPS." + warpName + ".pitch", playerLocation.getPitch());
                config.set("WARPS." + warpName + ".yaw", playerLocation.getYaw());
                try {
                    config.save(plugin.getDataFolder() + File.separator + "config.yml");
                    String warpSet = langsManager.getMessage(playerLanguage, "WARP_SET");
                    player.sendMessage(formatString(warpSet, warpName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, String warp){
        String warp_placeholder = "#warp#";
        return string.replaceAll(warp_placeholder, warp);
    }
}
