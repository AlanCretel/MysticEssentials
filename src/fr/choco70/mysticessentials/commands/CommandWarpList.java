package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandWarpList implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        Set<String> warps = config.getConfigurationSection("WARPS.").getKeys(false);
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(warps.size() == 0){
                String noWarps = localesManager.getMessage(playerLanguage, "NO_WARPS");
                player.sendMessage(noWarps);
            }
            else{
                String warpListMessage = localesManager.getMessage(playerLanguage, "WARP_LIST");
                player.sendMessage(formatString(warpListMessage, warps.toString()));
            }
        }
        else{
            if(warps.size() == 0){
                String noWarps = localesManager.getMessage(serverLanguage, "NO_WARPS");
                sender.sendMessage(noWarps);
            }
            else{
                String warpListMessage = localesManager.getMessage(serverLanguage, "WARP_LIST");
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
