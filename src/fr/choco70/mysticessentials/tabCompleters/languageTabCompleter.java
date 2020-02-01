package fr.choco70.mysticessentials.tabCompleters;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class languageTabCompleter implements TabCompleter{

    MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    langsManager langsManager = plugin.getLangsManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        ArrayList<String> proposals = new ArrayList<>();
        ArrayList<String> languages = langsManager.getAvailableLanguages();
        if(command.getName().equalsIgnoreCase("setlanguage")){
            if(arguments.length == 0){
                Collections.sort(languages);
                return languages;
            }
            else if(arguments.length == 1){
                for (String language : languages) {
                    if(language.startsWith(arguments[0])){
                        proposals.add(language);
                    }
                }
                Collections.sort(proposals);
                return proposals;
            }
        }
        return new ArrayList<>();
    }
}
