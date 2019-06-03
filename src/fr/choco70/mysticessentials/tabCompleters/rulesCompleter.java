package fr.choco70.mysticessentials.tabCompleters;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.rulesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class rulesCompleter implements TabCompleter{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private rulesManager rulesManager = plugin.getRulesManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length == 0){

            }
            else if(arguments.length == 1){

            }
        }
        return null;
    }
}
