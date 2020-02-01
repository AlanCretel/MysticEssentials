package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandSetLanguage implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        FileConfiguration serverLanguageConfig = YamlConfiguration.loadConfiguration(langsManager.getLanguageFile(serverLanguage));

        if(sender instanceof Player && arguments.length == 1){
            Player player = (Player)sender;
            FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
            String playerLanguage = playersManager.getPlayerLanguage(player);
            String newLanguage = arguments[0].toLowerCase();

            if(langsManager.getLanguageFile(arguments[0].toLowerCase()).exists() && !newLanguage.equalsIgnoreCase(playerLanguage)){
                playerConfig.set("language", newLanguage);
                playersManager.savePlayerConfig(player, playerConfig);
                String languageChanged = langsManager.getMessage(newLanguage, "LANGUAGE_CHANGED");
                player.sendMessage(formatString(languageChanged, player));
            }
            else if(playerLanguage.equalsIgnoreCase(newLanguage)){
                String sameLanguage = langsManager.getMessage(playerLanguage, "ALREADY_YOUR_LANGUAGE");
                player.sendMessage(formatString(sameLanguage, player));
            }
            else{
                String languageUnavailable = langsManager.getMessage(playerLanguage, "LANGUAGE_UNAVAILABLE");
                player.sendMessage(formatString(languageUnavailable, player));
            }
            return true;
        }
        else{
            if(sender instanceof Player && arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                String onlyPlayerCanUse = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
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
        String serverName = config.getString("SETTINGS.serverName", "A minecraft server");
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

        if(player != null){
            String formatedString = string.replaceAll(player_placeholder, player.getName());
            formatedString = formatedString.replaceAll(player_language_placeholder, playersManager.getPlayerLanguage(player));
            formatedString = formatedString.replaceAll(server_name_placeholder, serverName);
            formatedString = formatedString.replaceAll(server_language_placeholder, serverLanguage);
            return formatedString;
        }
        else{
            String formatedString = string.replaceAll(server_name_placeholder, serverName);
            formatedString = formatedString.replaceAll(server_language_placeholder, serverLanguage);
            return formatedString;
        }
    }
}
