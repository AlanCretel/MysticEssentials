package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandDelWarp implements CommandExecutor{

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
                if(config.isConfigurationSection("WARPS." + warpName)){
                    config.set("WARPS." + warpName, null);
                    String warpRemoved = langsManager.getMessage(playerLanguage, "WARP_REMOVED", "Successfully removed warp #warp#.");
                    player.sendMessage(formatString(warpRemoved, warpName));
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
            if(arguments.length == 1){
                String warpName = arguments[0];
                if(config.isConfigurationSection("WARPS." + warpName)){
                    config.set("WARPS." + warpName, null);
                    String warpRemoved = langsManager.getMessage(serverLanguage, "WARP_REMOVED", "Successfully removed warp #warp#.");
                    sender.sendMessage(formatString(warpRemoved, warpName));
                }
                else{
                    String noWarpFound = langsManager.getMessage(serverLanguage, "WARP_NOT_EXIST", "The warp #warp# does not exist.");
                    sender.sendMessage(formatString(noWarpFound, warpName));
                }
            }
            else{
                sender.sendMessage(command.getUsage());
            }
        }
        try {
            config.save(plugin.getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String formatString(String string, String warp){
        String warp_placeholder = "#warp#";
        return string.replaceAll(warp_placeholder, warp);
    }
}
