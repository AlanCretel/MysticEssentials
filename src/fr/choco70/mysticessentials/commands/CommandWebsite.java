package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandWebsite implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            String discordHead = langsManager.getMessage(playerLanguage, "WEBSITE_HEAD");
            String discordAddress = config.getString("SETTINGS.website", "No website available.");
            player.sendMessage(discordHead + discordAddress);
        }
        else{
            String discordHead = langsManager.getMessage(serverLanguage, "WEBSITE_HEAD");
            String discordAddress = config.getString("SETTINGS.website", "No website available.");
            sender.sendMessage(discordHead + discordAddress);
        }
        return true;
    }
}
