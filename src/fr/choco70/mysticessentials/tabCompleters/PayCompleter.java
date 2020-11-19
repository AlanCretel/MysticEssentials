package fr.choco70.mysticessentials.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PayCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 1){
            ArrayList<String> players = new ArrayList<>();
            Collection<? extends Player> onlinePlayers = sender.getServer().getOnlinePlayers();
            if(!args[0].equals("")){
                for (Player onlinePlayer : onlinePlayers) {
                    if(onlinePlayer.getName().startsWith(args[0])){
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
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(players.contains(player.getName())){
                    players.remove(player.getName());
                }
            }
            return players;
        }
        else if(args.length == 2){
            ArrayList<String> numbers = new ArrayList<>();
            for(int i = 0; i<10; i++){
                if(args[1] != null){
                    numbers.add(args[1] + i);
                }
                else{
                    if(i != 0){
                        numbers.add("" + i);
                    }
                }
            }
            Collections.reverse(numbers);
            return numbers;
        }
        return null;
    }
}
