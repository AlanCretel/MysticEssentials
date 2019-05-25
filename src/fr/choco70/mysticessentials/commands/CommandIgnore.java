package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandIgnore implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private playersManager playersManager = new playersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments != null && arguments.length == 1){
                if(player.getServer().getPlayer(arguments[0]) != null){
                    if(player.getServer().getPlayer(arguments[0]) != player){
                        Player ignored = player.getServer().getPlayer(arguments[0]);
                        if(ignored.hasPermission("mysticessentials.ignore.bypass")){
                            String cantIgnore = langsManager.getMessage(playersManager.getPlayerLanguage(player), "CANT_IGNORE", "You can't ignore #ignored#.");
                            player.sendMessage(formatString(cantIgnore, player, ignored.getName()));
                        }
                        else{
                            if(playersManager.getIgnoredPlayers(player) == null || !playersManager.getIgnoredPlayers(player).contains(ignored.getName())){
                                playersManager.addIgnoredPlayer(player, ignored);
                                String playerIgnored = langsManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_IGNORED", "You now ignore #ignored#.");
                                player.sendMessage(formatString(playerIgnored, player, ignored.getName()));
                                String nowIgnored = langsManager.getMessage(playersManager.getPlayerLanguage(ignored), "NOW_IGNORED", "You're now ignored by #player#.");
                                ignored.sendMessage(formatString(nowIgnored, player, ignored.getName()));
                            }
                            else{
                                playersManager.delIgnoredPlayer(player, ignored);
                                String playerUnignored = langsManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_UNIGNORED", "#ignored# is no more ignored.");
                                player.sendMessage(formatString(playerUnignored, player, ignored.getName()));
                                String nowUnignored = langsManager.getMessage(playersManager.getPlayerLanguage(ignored), "NOW_UNIGNORED", "You're no more ignored by #player#.");
                                ignored.sendMessage(formatString(nowUnignored, player, ignored.getName()));
                            }
                        }
                    }
                    else{
                        String selfRequested = langsManager.getMessage(playersManager.getPlayerLanguage(player), "SELF_IGNORED", "You can't ignore yourself.");
                        player.sendMessage(formatString(selfRequested, player, player.getName()));
                    }
                }
                else{
                    String playerNotFound = langsManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_NOT_FOUND", "Player #target# was not found.");
                    player.sendMessage(formatString(playerNotFound, player, arguments[0]));
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
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
