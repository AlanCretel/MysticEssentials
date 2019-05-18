package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandSetWarp implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", serverLanguage);
            if(arguments.length == 1){
                String warpName = arguments[0];
                Location playerLocation = player.getLocation();
                config.set("WARPS." + warpName + ".world", playerLocation.getWorld().getName());
                config.set("WARPS." + warpName + ".x", playerLocation.getX());
                config.set("WARPS." + warpName + ".y", playerLocation.getY());
                config.set("WARPS." + warpName + ".z", playerLocation.getZ());
                config.set("WARPS." + warpName + ".pitch", playerLocation.getPitch());
                config.set("WARPS." + warpName + ".yaw", playerLocation.getYaw());
                try {
                    config.save(plugin.getDataFolder() + "/config.yml");
                    String warpSet = langsManager.getMessage(playerLanguage, "WARP_SET", "Successfully set warp #warp#.");
                    player.sendMessage(formatString(warpSet, warpName));
                } catch (IOException e) {
                    e.printStackTrace();
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

    public String formatString(String string, String warp){
        String warp_placeholder = "#warp#";
        String formatedString = string.replaceAll(warp_placeholder, warp);
        return formatedString;
    }
}
