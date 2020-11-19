package fr.choco70.mysticessentials.tabCompleters;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WarpCompleter implements TabCompleter{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();

        ArrayList<String> warps = new ArrayList<>(config.getConfigurationSection("WARPS.").getKeys(false));
        return warps;
    }
}
