package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class CommandSetLanguage implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        FileConfiguration serverLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(serverLanguage));

        if(sender instanceof Player && arguments.length == 1){
            Player player = (Player)sender;
            UUID playerUUID = player.getUniqueId();

            plugin.createPlayerFile(playerUUID.toString());
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID.toString()));

            String playerLanguage = playerConfig.getString("language", "en_us");
            String newLanguage = arguments[0];

            if(plugin.getLanguageFile(arguments[0].toLowerCase()).exists() && newLanguage != playerLanguage){
                FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                String languageChangedBase = playerLanguageConfig.getString("LANGUAGE_CHANGED", "Language changed to #player_language#.");
                playerLanguageConfig.set("LANGUAGE_CHANGED", languageChangedBase);

                try {
                    playerLanguageConfig.save(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                playerConfig.set("language", arguments[0].toLowerCase());

                try {
                    playerConfig.save(plugin.getPlayerFile(playerUUID.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileConfiguration newPlayerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                String languageChanged = newPlayerLanguageConfig.getString("LANGUAGE_CHANGED", "Language changed to #player_language#.");
                player.sendMessage(formatString(languageChanged, player));
            }
            else if(playerLanguage == newLanguage){
                FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                String sameLanguage = playerLanguageConfig.getString("ALREADY_YOUR_LANGUAGE", "Language #player_language# is already your language.");
                player.sendMessage(formatString(sameLanguage, player));
                playerLanguageConfig.set("ALREADY_YOUR_LANGUAGE", sameLanguage);

                try {
                    playerLanguageConfig.save(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                String languageUnavailable = playerLanguageConfig.getString("LANGUAGE_UNAVAILABLE", "Language unavailable.");
                player.sendMessage(formatString(languageUnavailable, player));
                playerLanguageConfig.set("LANGUAGE_UNAVAILABLE", languageUnavailable);

                try {
                    playerLanguageConfig.save(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        else{
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                String onlyPlayerCanUse = serverLanguageConfig.getString("ONLY_PLAYERS_COMMAND", "Only players can use this command.");
                sender.sendMessage(formatString(onlyPlayerCanUse, null));
            }
            return true;
        }
    }

    public String formatString(String string, Player player){
        String player_placeholder = "#player#";
        String server_name_placeholder = "#server_name#";
        String player_language_placeholder = "#player_language#";
        String server_language_placeholder = "#server_language#";

        if(player != null){
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String formatedString = string.replaceAll(player_placeholder, player.getName());
            formatedString = formatedString.replaceAll(player_language_placeholder, playerConfig.getString("language", "en_us"));
            formatedString = formatedString.replaceAll(server_name_placeholder, config.getString("SETTINGS.serverName", "A minecraft server"));
            formatedString = formatedString.replaceAll(server_language_placeholder, config.getString("SETTINGS.serverLanguage", "en_us"));
            return formatedString;
        }
        else{
            String formatedString = string.replaceAll(server_name_placeholder, config.getString("SETTINGS.serverName", "A minecraft server"));
            formatedString = formatedString.replaceAll(server_language_placeholder, config.getString("SETTINGS.serverLanguage", "en_us"));
            return formatedString;
        }
    }
}
