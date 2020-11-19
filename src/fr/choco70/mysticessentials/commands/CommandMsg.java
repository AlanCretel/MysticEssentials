package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandMsg implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length >= 2){
                Player receiver = plugin.getServer().getPlayer(arguments[0]);
                if(receiver != null){
                    if(receiver != player && (playersManager.getPlayersIgnored(receiver) == null || !playersManager.getPlayersIgnored(receiver).contains(player.getUniqueId()))){
                        plugin.getConversations().put(receiver, player);
                        String senderMessagePrefix = localesManager.getMessage(playersManager.getPlayerLanguage(player), "MSG_PREFIX_SENDER");
                        String receiverMessagePrefix = localesManager.getMessage(playersManager.getPlayerLanguage(receiver), "MSG_PREFIX_RECEIVER");
                        String finalMessage = "";
                        for (int i = 1; i < arguments.length; i++) {
                            finalMessage = finalMessage + arguments[i] + " ";
                        }
                        player.sendMessage(formatString(senderMessagePrefix, receiver.getName()) + finalMessage);
                        receiver.sendMessage(formatString(receiverMessagePrefix, player) + finalMessage);
                        receiver.playSound(receiver.getLocation(), Sound.BLOCK_BELL_USE, 1, 1);
                    }
                    else if(playersManager.getPlayersIgnored(receiver).contains(player.getUniqueId())){
                        String ignored = localesManager.getMessage(playersManager.getPlayerLanguage(player), "IGNORED");
                        player.sendMessage(formatString(ignored, receiver.getName()));
                    }
                    else{
                        player.sendMessage(localesManager.getMessage(playersManager.getPlayerLanguage(player), "SELF_MESSAGE"));
                    }
                }
                else{
                    String playerNotFound = localesManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, arguments[0]));
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            sender.sendMessage(localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND"));
        }
        return true;
    }

    public String formatString(String string, String receiver){
        String receiver_placeholder = "#receiver#";
        String target_placeholder = "#target#";
        String player_placeholder = "#player#";
        return string.replaceAll(receiver_placeholder, receiver).replaceAll(target_placeholder, receiver).replaceAll(player_placeholder, receiver);
    }

    public String formatString(String string, Player player){
        String sender_placeholder = "#sender#";
        return string.replaceAll(sender_placeholder, player.getDisplayName());
    }
}
