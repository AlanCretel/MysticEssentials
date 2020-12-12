package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandWarpList implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        ArrayList<String> warps = sqLiteManager.getWarps();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
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
