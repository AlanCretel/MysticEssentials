package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandSetSpawn implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            config.set("SPAWN.world", player.getWorld().getName());
            config.set("SPAWN.x", player.getLocation().getX());
            config.set("SPAWN.y", player.getLocation().getY());
            config.set("SPAWN.z", player.getLocation().getZ());
            config.set("SPAWN.pitch", player.getLocation().getPitch());
            config.set("SPAWN.yaw", player.getLocation().getYaw());
            try {
                config.save(plugin.getDataFolder().toString() + "/config.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage("Spawn point set.");
            return true;
        }
        else{
            sender.sendMessage("You need to be a player to set the spawn.");
            return false;
        }
    }
}
