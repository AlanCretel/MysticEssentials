package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 1 && arguments.length != 0){
                player.sendMessage(command.getUsage());
                return true;
            }
            else if(arguments.length == 0){
                addHome(player, "home");
                return true;
            }
            else{
                String homeName = arguments[0];
                addHome(player, homeName);
                return true;
            }
        }
        else{
            sender.sendMessage("Only players can use this command.");
            return false;
        }
    }

    public void addHome(Player player, String homeName){
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));

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
        player.sendMessage("Successfully set home " + homeName + ".");
    }
}
