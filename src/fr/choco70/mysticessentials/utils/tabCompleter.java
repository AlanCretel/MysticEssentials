package fr.choco70.mysticessentials.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class tabCompleter implements TabCompleter{

    private playersManager playersManager = new playersManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        if(command.getName().equalsIgnoreCase("home") && sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length == 1){
                ArrayList<String> homes = new ArrayList<String>();
                Set<String> homeList = playersManager.getHomeList(player);
                if(!arguments[0].equals("")){
                    for (String s1 : homeList) {
                        if(s1.toLowerCase().startsWith(arguments[0].toLowerCase())){
                            homes.add(s1);
                        }
                    }
                }
                else{
                    homes.addAll(homeList);
                }
                Collections.sort(homes);
                return homes;
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}