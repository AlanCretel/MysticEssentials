package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandDelSpawn implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(config.isConfigurationSection("SPAWN")){
            config.set("SPAWN", null);
            sender.sendMessage("Successfully removed spawn location.");
        }
        else{
            sender.sendMessage("No spawn was set.");
        }

        return false;
    }
}
