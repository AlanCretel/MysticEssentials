package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandReload implements CommandExecutor{
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));

            String configReloaded = playerLanguageConfig.getString("CONFIG_RELOADED", "MysticEssentials's configuration reloaded");
            player.sendMessage(configReloaded);
            playerLanguageConfig.set("CONFIG_RELOADED", configReloaded);

            try {
                playerLanguageConfig.save(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            FileConfiguration serverLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us")));

            String configReloaded = serverLanguageConfig.getString("CONFIG_RELOADED", "MysticEssentials's configuration reloaded");
            sender.sendMessage(configReloaded);
            serverLanguageConfig.set("CONFIG_RELOADED", configReloaded);

            try {
                serverLanguageConfig.save(plugin.getLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        plugin.reloadConfig();
        try {
            config.load(plugin.getDataFolder() + "/config.yml");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }
}
