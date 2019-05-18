package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandWarpList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        Set<String> warps = config.getConfigurationSection("WARPS.").getKeys(false);
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", serverLanguage);
            if(warps.size() == 0){
                String noWarps = langsManager.getMessage(playerLanguage, "NO_WARPS", "No warps were set.");
                player.sendMessage(noWarps);
            }
            else{
                String warpListMessage = langsManager.getMessage(playerLanguage, "WARP_LIST", "Available warps: #warp_list#.");
                player.sendMessage(formatString(warpListMessage, warps.toString()));
            }
        }
        else{
            if(warps.size() == 0){
                String noWarps = langsManager.getMessage(serverLanguage, "NO_WARPS", "No warps were set.");
                sender.sendMessage(noWarps);
            }
            else{
                String warpListMessage = langsManager.getMessage(serverLanguage, "WARP_LIST", "Available warps: #warp_list#.");
                sender.sendMessage(formatString(warpListMessage, warps.toString()));
            }
        }
        return true;
    }

    public String formatString(String string, String warps){
        String warpList_placeholder = "#warp_list#";
        String formatedString = string.replaceAll(warpList_placeholder, warps);

        return formatedString;
    }
}
