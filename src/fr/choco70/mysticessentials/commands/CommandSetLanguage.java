package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandSetLanguage implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        String serverLanguage = localesManager.getServerLocale();

        if(sender instanceof Player && arguments.length == 1){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            String newLanguage = arguments[0].toLowerCase();

            if(localesManager.getLocaleFile(newLanguage).exists() && !newLanguage.equalsIgnoreCase(playerLanguage)){
                sqLiteManager.updatePlayerLocale(player.getUniqueId(), newLanguage);
                String languageChanged = localesManager.getMessage(newLanguage, "LANGUAGE_CHANGED");
                player.sendMessage(formatString(languageChanged, player));
            }
            else if(playerLanguage.equalsIgnoreCase(newLanguage)){
                String sameLanguage = localesManager.getMessage(playerLanguage, "ALREADY_YOUR_LANGUAGE");
                player.sendMessage(formatString(sameLanguage, player));
            }
            else{
                String languageUnavailable = localesManager.getMessage(playerLanguage, "LANGUAGE_UNAVAILABLE");
                player.sendMessage(formatString(languageUnavailable, player));
            }
        }
        else{
            if(sender instanceof Player && arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                String onlyPlayerCanUse = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
                sender.sendMessage(formatString(onlyPlayerCanUse, null));
            }
        }
        return true;
    }

    public String formatString(String string, Player player){
        String player_placeholder = "#player#";
        String server_name_placeholder = "#server_name#";
        String player_language_placeholder = "#player_language#";
        String server_language_placeholder = "#server_language#";
        String serverName = config.getString("SETTINGS.serverName", "A minecraft server");
        String serverLanguage = localesManager.getServerLocale();

        if(player != null){
            String formatedString = string.replaceAll(player_placeholder, player.getName());
            formatedString = formatedString.replaceAll(player_language_placeholder, sqLiteManager.getPlayerLocale(player.getUniqueId()));
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
