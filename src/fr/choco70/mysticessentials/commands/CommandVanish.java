package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVanish implements CommandExecutor{

    MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(sender instanceof Player){
            Player player = (Player)sender;
            plugin.getServer().getOnlinePlayers().remove(player);
            player.setCanPickupItems(false);

        }
        else{
            sender.sendMessage(localesManager.getMessage(localesManager.getServerLocale(), "ONLY_PLAYERS_COMMAND"));
        }

        return true;
    }
}
