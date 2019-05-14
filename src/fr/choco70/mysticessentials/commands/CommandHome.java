package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandHome implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerUUID = player.getUniqueId().toString();

            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));
            Set<String> homes = playerConfig.getConfigurationSection("homes").getKeys(false);

            if(!(arguments.length == 0)){
                if(arguments.length == 1){
                    String homeName = arguments[0];
                    if(playerConfig.isConfigurationSection("homes." + homeName)){
                        toHome(player, homeName);
                        return true;
                    }
                    else{
                        player.sendMessage("You don't have any home called " + homeName + ".");
                        return true;
                    }
                }
                else{
                    player.sendMessage(command.getUsage());
                    return true;
                }
            }
            else{
                if(homes != null && homes.size() != 0){
                    if(homes.contains("home")){
                        toHome(player, "home");
                    }
                    else{
                        player.sendMessage("You homes: " + homes.toString());
                    }
                    return true;
                }
                else{
                    player.sendMessage("You don't have any home.");
                    return true;
                }
            }
        }
        else{
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }

    public void toHome(Player player, String homeName){
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));

        player.sendMessage("Teleportation to home " + homeName + "...");

        Double x = Double.valueOf(playerConfig.get("homes." + homeName + ".x").toString());
        Double y = Double.valueOf(playerConfig.get("homes." + homeName + ".y").toString());
        Double z = Double.valueOf(playerConfig.get("homes." + homeName + ".z").toString());
        Float pitch = Float.valueOf(playerConfig.get("homes." + homeName + ".pitch").toString());
        Float yaw = Float.valueOf(playerConfig.get("homes." + homeName + ".yaw").toString());
        World world = player.getServer().getWorld(playerConfig.get("homes." + homeName + ".world").toString());

        Location homeLocation = player.getLocation().clone();

        homeLocation.setWorld(world);
        homeLocation.setX(x);
        homeLocation.setY(y);
        homeLocation.setZ(z);
        homeLocation.setPitch(pitch);
        homeLocation.setYaw(yaw);

        player.teleport(homeLocation);
    }
}
