package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandWarpList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = langsManager.getServerLanguage();
        Set<String> warps = config.getConfigurationSection("WARPS.").getKeys(false);
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(warps.size() == 0){
                String noWarps = langsManager.getMessage(playerLanguage, "NO_WARPS");
                player.sendMessage(noWarps);
            }
            else{
                String warpListMessage = langsManager.getMessage(playerLanguage, "WARP_LIST");
                player.sendMessage(formatString(warpListMessage, warps.toString()));
            }
        }
        else{
            if(warps.size() == 0){
                String noWarps = langsManager.getMessage(serverLanguage, "NO_WARPS");
                sender.sendMessage(noWarps);
            }
            else{
                String warpListMessage = langsManager.getMessage(serverLanguage, "WARP_LIST");
                sender.sendMessage(formatString(warpListMessage, warps.toString()));
            }
        }
        return true;
    }

    public String formatString(String string, String warps){
        String warpList_placeholder = "#warp_list#";
        return string.replaceAll(warpList_placeholder, warps);
    }
}
