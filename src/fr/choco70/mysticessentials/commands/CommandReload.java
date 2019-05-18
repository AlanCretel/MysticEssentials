package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor{
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", "en_us");

            String configReloaded = langsManager.getMessage(playerLanguage, "CONFIG_RELOADED", "MysticEssentials's configuration reloaded");
            player.sendMessage(configReloaded);
        }
        else{
            String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

            String configReloaded = langsManager.getMessage(serverLanguage, "CONFIG_RELOADED", "MysticEssentials's configuration reloaded");
            sender.sendMessage(configReloaded);
        }
        return true;
    }
}
