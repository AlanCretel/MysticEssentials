package fr.choco70.mysticessentials.tabCompleters;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarpCompleter implements TabCompleter{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args){
        ArrayList<String> warps = sqLiteManager.getWarps();
        ArrayList<String> proposals = new ArrayList<>();
        if(args != null){
            if(args.length == 1){
                for (String warp : warps) {
                    if(warp.startsWith(args[0])){
                        proposals.add(warp);
                    }
                }
                Collections.sort(proposals);
            }
        }
        return proposals;
    }
}
