package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpa implements CommandExecutor {

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length != 1){
                return false;
            }
            else if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null && target.isOnline() && target != player){
                    if(!plugin.getTpa().containsKey(player)){
                        target.sendMessage(player.getName() + " requested to teleport to you.\n Type /tpaccept to accept them.\n Type /tpdeny to deny them.");
                        player.sendMessage("Sent a teleport request to " + target.getName() + ".");
                        plugin.getTpa().put(target, player);
                        return true;
                    }
                    else{
                        player.sendMessage("You already sent a teleportation request to " + target.getName());
                        return true;
                    }
                }
                else if(player == target){
                    player.sendMessage("You can't send a request to yourself.");
                    return true;
                }
            }
        }
        return false;
    }

}
