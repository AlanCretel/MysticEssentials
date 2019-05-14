package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandTpAccept implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                if(requester != null && requester.isOnline()){
                    requester.teleport(player.getLocation());
                    plugin.getTpa().remove(player);
                    requester.sendMessage("Successfully teleported to " + player.getName() + ".");
                }
                else{
                    player.sendMessage("Player " + requester.getName() + " is offline.");
                    plugin.getTpa().remove(player);
                }
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                if(requester != null && requester.isOnline()){
                    player.teleport(requester.getLocation());
                    plugin.getTpahere().remove(player);
                    requester.sendMessage("Successfully teleported " + player.getName() + " to you.");
                }
                else{
                    player.sendMessage("Player " + requester.getName() + " is offline.");
                    plugin.getTpahere().remove(player);
                }
            }
            else{
                player.sendMessage("You don't have any teleportation request.");
            }
        }
        else{
            FileConfiguration serverLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(config.getString("SETTINGS.serverLanguage", "en_us")));
            String onlyPlayersWarn = serverLanguageConfig.getString("ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            serverLanguageConfig.set("ONLY_PLAYERS_COMMAND", onlyPlayersWarn);

            plugin.saveLanguageConfig(serverLanguageConfig, config.getString("SETTINGS.serverLanguage", "en_us"));

            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
