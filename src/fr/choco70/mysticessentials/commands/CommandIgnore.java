package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandIgnore implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments != null && arguments.length == 1){
                if(player.getServer().getPlayer(arguments[0]) != null){
                    if(player.getServer().getPlayer(arguments[0]) != player){
                        Player ignored = player.getServer().getPlayer(arguments[0]);
                        if(ignored.hasPermission("mysticessentials.ignore.bypass")){
                            String cantIgnore = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "CANT_IGNORE");
                            player.sendMessage(formatString(cantIgnore, player, ignored.getName()));
                        }
                        else{
                            if(sqLiteManager.getIgnoredPlayers(player.getUniqueId()) == null || !sqLiteManager.doesIgnorePlayer(player.getUniqueId(), ignored.getUniqueId())){
                                sqLiteManager.setIgnorePlayer(player.getUniqueId(), ignored.getUniqueId());
                                String playerIgnored = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "PLAYER_IGNORED");
                                player.sendMessage(formatString(playerIgnored, player, ignored.getName()));
                                String nowIgnored = localesManager.getMessage(sqLiteManager.getPlayerLocale(ignored.getUniqueId()), "NOW_IGNORED");
                                ignored.sendMessage(formatString(nowIgnored, player, ignored.getName()));
                            }
                            else{
                                sqLiteManager.removeIgnoredPlayer(player.getUniqueId(), ignored.getUniqueId());
                                String playerUnignored = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "PLAYER_UNIGNORED");
                                player.sendMessage(formatString(playerUnignored, player, ignored.getName()));
                                String nowUnignored = localesManager.getMessage(sqLiteManager.getPlayerLocale(ignored.getUniqueId()), "NOW_UNIGNORED");
                                ignored.sendMessage(formatString(nowUnignored, player, ignored.getName()));
                            }
                        }
                    }
                    else{
                        String selfRequested = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "SELF_IGNORED");
                        player.sendMessage(formatString(selfRequested, player, player.getName()));
                    }
                }
                else{
                    String playerNotFound = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, player, arguments[0]));
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Player player, String targetName){
        String target_placeholder = "#target#";
        String player_placeholder = "#player#";
        String ignored_placeholder = "#ignored#";

        String formatedString = string.replaceAll(target_placeholder, targetName);
        formatedString = formatedString.replaceAll(player_placeholder, player.getName());
        formatedString = formatedString.replaceAll(ignored_placeholder, targetName);

        return formatedString;
    }
}
