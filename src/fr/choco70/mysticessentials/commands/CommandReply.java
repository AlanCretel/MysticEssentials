package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandReply implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length >= 1){
                if(plugin.getConversations().containsKey(player)){
                    Player receiver = plugin.getConversations().get(player);
                    if(receiver.isOnline()){
                        plugin.getConversations().put(receiver, player);
                        String senderMessagePrefix = langsManager.getMessage(playersManager.getPlayerLanguage(player), "REPLY_PREFIX_SENDER", "Message to #receiver#: ");
                        String receiverMessagePrefix = langsManager.getMessage(playersManager.getPlayerLanguage(receiver), "REPLY_PREFIX_RECEIVER", "#sender# to you: ");
                        String finalMessage = "";
                        for (int i = 0; i < arguments.length; i++) {
                            finalMessage = finalMessage + arguments[i];
                        }
                        player.sendMessage(formatString(senderMessagePrefix, receiver.getName()) + finalMessage);
                        receiver.sendMessage(formatString(receiverMessagePrefix, player) + finalMessage);
                        receiver.playSound(receiver.getLocation(), Sound.BLOCK_BELL_USE, 1, 1);
                    }
                    else{
                        String playerOffline = langsManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_OFFLINE", "Player #receiver# is offline.");
                        player.sendMessage(formatString(playerOffline, receiver.getName()));
                    }
                }
                else{
                    String noConversationToReply = langsManager.getMessage(playersManager.getPlayerLanguage(player), "NO_CONVERSATION_TO_REPLY", "You don't have any conversation to which to reply.");
                    player.sendMessage(noConversationToReply);
                }
            }
            else{
                player.sendMessage(command.getUsage());
            }
        }
        else{
            sender.sendMessage(langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command."));
        }
        return true;
    }

    public String formatString(String string, String receiver){
        String receiver_placeholder = "#receiver#";
        String target_placeholder = "#target#";
        return string.replaceAll(receiver_placeholder, receiver).replaceAll(target_placeholder, receiver);
    }

    public String formatString(String string, Player player){
        String sender_placeholder = "#sender#";
        return string.replaceAll(sender_placeholder, player.getDisplayName());
    }
}
