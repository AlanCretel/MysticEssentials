package fr.choco70.mysticessentials.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class NoCompleter implements TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments) {
        return new ArrayList<String>();
    }
}
