package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandDelHome implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 0 && arguments.length == 1){
                String homeName = arguments[0];
                delHome(player, homeName);
                return true;
            }
            else if(arguments.length == 0){
                delHome(player, "home");
                return true;
            }
            else{
                player.sendMessage(command.getUsage());
                return true;
            }
        }
        else{
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }

    public void delHome(Player player, String homeName){
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));

        if(playerConfig.isConfigurationSection("homes." + homeName)){
            playerConfig.set("homes." + homeName, null);
            try {
                playerConfig.save(plugin.getPlayerFile(player.getUniqueId().toString()));
                player.sendMessage("Home " + homeName + " successfully removed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            player.sendMessage("You don't have a home called " + homeName + ".");
        }
    }
}
