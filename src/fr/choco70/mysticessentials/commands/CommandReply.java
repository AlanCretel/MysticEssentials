package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReply implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLocale = sqLiteManager.getPlayerLocale(player.getUniqueId());
            if(arguments.length >= 1){
                if(plugin.getConversations().containsKey(player)){
                    Player receiver = plugin.getConversations().get(player);
                    if(receiver.isOnline()){
                        plugin.getConversations().put(receiver, player);
                        String senderMessagePrefix = localesManager.getMessage(playerLocale, "REPLY_PREFIX_SENDER");
                        String receiverMessagePrefix = localesManager.getMessage(sqLiteManager.getPlayerLocale(receiver.getUniqueId()), "REPLY_PREFIX_RECEIVER");
                        String finalMessage = "";
                        for (int i = 0; i < arguments.length; i++) {
                            finalMessage = finalMessage + arguments[i] + " ";
                        }
                        player.sendMessage(formatString(senderMessagePrefix, receiver.getName()) + finalMessage);
                        receiver.sendMessage(formatString(receiverMessagePrefix, player) + finalMessage);
                        receiver.playSound(receiver.getLocation(), Sound.BLOCK_BELL_USE, 1, 1);
                    }
                    else{
                        String playerOffline = localesManager.getMessage(playerLocale, "PLAYER_OFFLINE");
                        player.sendMessage(formatString(playerOffline, receiver.getName()));
                    }
                }
                else{
                    String noConversationToReply = localesManager.getMessage(playerLocale, "NO_CONVERSATION_TO_REPLY");
                    player.sendMessage(noConversationToReply);
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
        return string.replaceAll(receiver_placeholder, receiver).replaceAll(target_placeholder, receiver);
    }

    public String formatString(String string, Player player){
        String sender_placeholder = "#sender#";
        return string.replaceAll(sender_placeholder, player.getDisplayName());
    }
}
