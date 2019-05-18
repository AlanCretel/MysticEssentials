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

public class CommandSetHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 1 && arguments.length != 0){
                player.sendMessage(command.getUsage());
            }
            else if(arguments.length == 0){
                addHome(player, "home");
            }
            else{
                String homeName = arguments[0];
                addHome(player, homeName);
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public void addHome(Player player, String homeName){
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
        String playerLanguage = playerConfig.getString("language", serverLanguage);
        Location playerLocation = player.getLocation();

        playerConfig.set("homes." + homeName + ".world", player.getWorld().getName());
        playerConfig.set("homes." + homeName + ".x", playerLocation.getX());
        playerConfig.set("homes." + homeName + ".y", playerLocation.getY());
        playerConfig.set("homes." + homeName + ".z", playerLocation.getZ());
        playerConfig.set("homes." + homeName + ".pitch", playerLocation.getPitch());
        playerConfig.set("homes." + homeName + ".yaw", playerLocation.getYaw());

        try {
            playerConfig.save(plugin.getPlayerFile(player.getUniqueId().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String homeSet = langsManager.getMessage(playerLanguage, "HOME_SET", "Successfully set home #home#.");
        player.sendMessage(formatString(homeSet, homeName));
    }

    public String formatString(String string, String homeName){
        String homeName_placeholder = "#home#";
        String formatedString = string.replaceAll(homeName_placeholder, homeName);
        return formatedString;
    }
}
