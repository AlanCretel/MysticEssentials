package fr.choco70.mysticessentials.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EcoCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> proposals = new ArrayList<>();
        if(args.length == 1){
            ArrayList<String> subCommands = new ArrayList<>();
            subCommands.add("set");
            subCommands.add("add");
            subCommands.add("remove");
            subCommands.add("default");
            if(args[0] != null){
                for (String subCommand : subCommands) {
                    if(subCommand.startsWith(args[0])){
                        proposals.add(subCommand);
                    }
                }
            }
            else{
                proposals.addAll(subCommands);
            }
            return proposals;
        }
        else if(args.length == 2){
            ArrayList<String> players = new ArrayList<>();
            Collection<? extends Player> onlinePlayers = sender.getServer().getOnlinePlayers();
            if(!args[1].equals("")){
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
            return players;
        }
        else if(args.length == 3 && !(args[1].equalsIgnoreCase("default"))){
            ArrayList<String> numbers = new ArrayList<>();
            for(int i = 0; i<10; i++){
                if(args[2] != null){
                    numbers.add(args[2] + i);
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
        else{
            return new ArrayList<>();
        }
    }
}
