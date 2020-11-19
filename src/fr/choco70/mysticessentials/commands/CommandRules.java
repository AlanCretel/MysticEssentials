package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import fr.choco70.mysticessentials.utils.RulesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandRules implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private PlayersManager playersManager = plugin.getPlayersManager();
    private RulesManager rulesManager = plugin.getRulesManager();
    private LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            ArrayList<String> rules = rulesManager.getRulesList();
            if(rules.size() == 0){
                player.sendMessage(localesManager.getMessage(playerLanguage, "NO_RULES"));
            }
            else{
                if(rules.size() == 1 && rules.get(0).equals("")){
                    player.sendMessage(localesManager.getMessage(playerLanguage, "NO_RULES"));
                }
                else{
                    player.sendMessage(localesManager.getMessage(playerLanguage, "RULES_HEAD"));
                    for (int i = 0; i < rules.size(); i++) {
                        String rulePrefix = "  " + (i+1) + ": ";
                        player.sendMessage(rulePrefix + rulesManager.getRule(i));
                    }
                }
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
