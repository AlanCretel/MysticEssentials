package fr.choco70.mysticessentials.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class msgCompleter implements TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        if(command.getName().equalsIgnoreCase("msg") && sender instanceof Player){
            if(arguments.length == 1){
                ArrayList<String> players = new ArrayList<>();
                Collection<? extends Player> onlinePlayers = sender.getServer().getOnlinePlayers();
                if(!arguments[0].equals("")){
                    for (Player onlinePlayer : onlinePlayers) {
                        if(onlinePlayer.getName().startsWith(arguments[0])){
                            players.add(onlinePlayer.getName());
                        }
                    }
                }
                else{
                    for (Player onlinePlayer : onlinePlayers) {
                        players.add(onlinePlayer.getName());
                    }
                }
                Collections.sort(players);
                return players;
            }
            else{
                return new ArrayList<String>();
            }
        }
        else{
            return null;
        }
    }
}
