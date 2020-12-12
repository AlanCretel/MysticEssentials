package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandWebsite implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        String serverLanguage = localesManager.getServerLocale();

        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            String discordHead = localesManager.getMessage(playerLanguage, "WEBSITE_HEAD");
            String discordAddress = config.getString("SETTINGS.website", "No website available.");
            player.sendMessage(discordHead + discordAddress);
        }
        else{
            String discordHead = localesManager.getMessage(serverLanguage, "WEBSITE_HEAD");
            String discordAddress = config.getString("SETTINGS.website", "No website available.");
            sender.sendMessage(discordHead + discordAddress);
        }
        return true;
    }
}
