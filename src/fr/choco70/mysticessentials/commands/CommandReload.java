package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor{
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            String configReloaded = localesManager.getMessage(playerLanguage, "CONFIG_RELOADED");
            player.sendMessage(configReloaded);
        }
        else{
            String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
            String configReloaded = localesManager.getMessage(serverLanguage, "CONFIG_RELOADED");
            sender.sendMessage(configReloaded);
        }
        return true;
    }
}
