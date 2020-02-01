package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandTpAll implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        String senderLanguage;

        ArrayList<Player> onlinePlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        if(sender instanceof Player){
            Player player = (Player)sender;
            senderLanguage = playersManager.getPlayerLanguage(player);
            onlinePlayers.remove(player);
        }
        else{
            senderLanguage = langsManager.getServerLanguage();
        }

        if(onlinePlayers.size() == 0){
            sender.sendMessage(langsManager.getMessage(senderLanguage, "NOT_ENOUGH_PLAYERS_ONLINE"));
            return true;
        }

        if(args != null){
            if(args.length == 1){
                Player target = plugin.getServer().getPlayer(args[0]);
                if(target != null){
                    onlinePlayers.remove(target);
                    Location teleportLocation = target.getLocation();
                    if(sender instanceof Player){
                        Player player = (Player)sender;
                        if(target == player){
                            sender.sendMessage(formatMessage(langsManager.getMessage(senderLanguage, "TPALL_SELF"), target));
                        }
                        else{
                            sender.sendMessage(formatMessage(langsManager.getMessage(senderLanguage, "TPALL_SENDER"), target));
                            target.sendMessage(formatMessage(langsManager.getMessage(playersManager.getPlayerLanguage(target), "TPALL_TARGET"), target));
                        }
                        for(Player onlinePlayer : onlinePlayers){
                            onlinePlayer.teleport(teleportLocation);
                            onlinePlayer.sendMessage(formatMessage(langsManager.getMessage(playersManager.getPlayerLanguage(onlinePlayer), "TPALL_TELEPORTED"), target));
                        }
                    }
                    else{
                        sender.sendMessage(formatMessage(langsManager.getMessage(senderLanguage, "TPALL_SENDER"), target));
                        target.sendMessage(formatMessage(langsManager.getMessage(playersManager.getPlayerLanguage(target), "TPALL_TARGET"), target));
                        for(Player onlinePlayer : onlinePlayers){
                            onlinePlayer.teleport(teleportLocation);
                            onlinePlayer.sendMessage(formatMessage(langsManager.getMessage(playersManager.getPlayerLanguage(onlinePlayer), "TPALL_TELEPORTED"), target));
                        }
                    }
                }
                else{
                    sender.sendMessage(formatMessage(langsManager.getMessage(senderLanguage, "PLAYER_NOT_FOUND"), target));
                }
            }
            else{
                sender.sendMessage(command.getUsage());
            }
        }
        else{
            sender.sendMessage(command.getUsage());
        }
        return true;
    }

    private String formatMessage(String message, Player target){
        String targetPlaceHolder = "%target%";
        return message.replaceAll(targetPlaceHolder, target.getDisplayName());
    }

}
