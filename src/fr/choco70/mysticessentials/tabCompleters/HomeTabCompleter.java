package fr.choco70.mysticessentials.tabCompleters;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements TabCompleter{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        if((command.getName().equalsIgnoreCase("home") || command.getName().equalsIgnoreCase("delhome") || command.getName().equalsIgnoreCase("sethome") || command.getName().equalsIgnoreCase("setdefaulthome")) && sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length == 1 && sqLiteManager.getHomes(player.getUniqueId()) != null){
                ArrayList<String> homes = new ArrayList<String>();
                ArrayList<String> homeList = sqLiteManager.getHomes(player.getUniqueId());
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
