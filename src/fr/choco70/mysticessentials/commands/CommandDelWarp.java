package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CommandDelWarp implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(arguments.length == 1){
                String warpName = arguments[0];
                removeWarp(warpName, playerLanguage, sender);
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            if(arguments.length == 1){
                String warpName = arguments[0];
                removeWarp(warpName, serverLanguage, sender);
            }
            else{
                sender.sendMessage(command.getUsage());
            }
        }
        return true;
    }

    public void removeWarp(String warpName, String senderLocale, CommandSender sender){
        if(sqLiteManager.warpExist(warpName)){
            sqLiteManager.removeWarp(warpName);
            String warpRemoved = localesManager.getMessage(senderLocale, "WARP_REMOVED");
            sender.sendMessage(formatString(warpRemoved, warpName));
        }
        else{
            String noWarpFound = localesManager.getMessage(senderLocale, "WARP_NOT_EXIST");
            sender.sendMessage(formatString(noWarpFound, warpName));
        }
        try {
            config.save(plugin.getDataFolder() + File.separator + "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatString(String string, String warp){
        String warp_placeholder = "#warp#";
        return string.replaceAll(warp_placeholder, warp);
    }
}
