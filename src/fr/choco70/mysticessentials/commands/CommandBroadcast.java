package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;

public class CommandBroadcast implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String broadcastPrefix = config.getString("SETTINGS.broadcastPrefix", "§7[§4#server_name#§7]§f: ");
        String broadcast = "";
        for (String argument : arguments) {
            broadcast = broadcast.concat(argument + " ");
        }
        sender.getServer().broadcastMessage(formatString(broadcastPrefix, sender) + broadcast);
        Collection<? extends Player> onlinePlayers = sender.getServer().getOnlinePlayers();
        for (Player onlinePlayer : onlinePlayers) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        }
        return true;
    }

    public String formatString(String string, CommandSender sender){
        String serverName_placeholder = "#server_name#";
        String sender_placeholder = "#sender#";
        String serverName = config.getString("SETTINGS.serverName", "A minecraft server");
        String formatedString = string.replaceAll(serverName_placeholder, serverName);
        if(sender instanceof Player){
            Player player = (Player)sender;
            return formatedString.replaceAll(sender_placeholder, player.getName());
        }
        else{
            return formatedString;
        }
    }
}
