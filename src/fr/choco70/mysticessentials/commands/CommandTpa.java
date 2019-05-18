package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandTpa implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", serverLanguage);
            if(arguments.length != 1){
                player.sendMessage(command.getUsage());
            }
            else if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null && target.isOnline() && target != player){
                    FileConfiguration targetConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(target.getUniqueId().toString()));
                    String targetLanguage = targetConfig.getString("language", serverLanguage);
                    String targetName = target.getName();
                    if(!plugin.getTpa().containsKey(player)){
                        String tpaRequestTarget = langsManager.getMessage(targetLanguage, "TPA_REQUEST_TARGET", "#requester# requested to teleport to you./n Type /tpaccept to accept them./n Type /tpdeny to deny them.");
                        target.sendMessage(formatString(tpaRequestTarget, player, targetName));

                        String tpaRequestSender = langsManager.getMessage(playerLanguage, "TPA_REQUEST_SENDER", "Sent a teleportation request to #target#.");
                        player.sendMessage(formatString(tpaRequestSender, player, targetName));
                        plugin.getTpa().put(target, player);
                    }
                    else{
                        String requestAlreadySent = langsManager.getMessage(playerLanguage, "REQUEST_ALREADY_SENT", "You already sent a teleportation request to #target#.");
                        player.sendMessage(formatString(requestAlreadySent, player, targetName));
                    }
                }
                else if(player == target){
                    String selfRequested = langsManager.getMessage(playerLanguage, "SELF_REQUESTED", "You can't send a request to yourself.");
                    player.sendMessage(formatString(selfRequested, player, player.getName()));
                }
                else if(target == null){
                    String playerNotFound = langsManager.getMessage(playerLanguage, "PLAYER_NOT_FOUND", "Player #target# was not found.");
                    player.sendMessage(formatString(playerNotFound, player, arguments[0]));
                }
                else{
                    String playerOffline = langsManager.getMessage(playerLanguage, "PLAYER_OFFLINE", "Player #target# is offline.");
                    player.sendMessage(formatString(playerOffline, player, arguments[0]));
                }
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
        String sender_placeholder = "#sender#";
        String newLine_placeholder = "/n";

        String formatedString = string.replaceAll(target_placeholder, targetName);
        formatedString = formatedString.replaceAll(sender_placeholder, player.getName());
        formatedString = formatedString.replaceAll(newLine_placeholder, "\n");

        return formatedString;
    }

}
