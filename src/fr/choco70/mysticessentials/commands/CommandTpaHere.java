package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandTpaHere implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 1){
                player.sendMessage(command.getUsage());
            }
            else if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null && target.isOnline()){
                    if(!plugin.getTpahere().containsKey(player)){
                        target.sendMessage(player.getName() + " requested you to teleport to them.\n Type /tpaccept to accept them.\n Type /tpdeny to deny them.");
                        player.sendMessage("Sent a teleport request to " + target.getName() + ".");
                        plugin.getTpahere().put(target, player);
                    }
                    else{
                        player.sendMessage("You already sent a teleportation request to " + target.getName());
                    }
                }
                else if(player == target){
                    player.sendMessage("You can't send a request to yourself.");
                }
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
