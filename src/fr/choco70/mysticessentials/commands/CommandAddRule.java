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

public class CommandAddRule implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private RulesManager rulesManager = plugin.getRulesManager();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private LocalesManager localesManager = plugin.getLocalesManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        if(sender instanceof Player){
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                Player player = (Player)sender;
                String newRule = "";
                for (String argument : arguments) {
                    newRule = newRule + argument + " ";
                }
                if(rulesManager.getRulesNumber() == 1 && rulesManager.getRule(0).equals("")){
                    rulesManager.setRule(0, newRule);
                }
                else{
                    rulesManager.addRule(newRule);
                }
                player.sendMessage(localesManager.getMessage(playersManager.getPlayerLanguage(player), "RULE_ADDED"));
            }
        }
        else{
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                String newRule = "";
                for (String argument : arguments) {
                    newRule = newRule + argument + " ";
                }
                if(rulesManager.getRulesNumber() == 1 && rulesManager.getRule(0).equals("")){
                    rulesManager.setRule(0, newRule);
                }
                else{
                    rulesManager.addRule(newRule);
                }
                sender.sendMessage(localesManager.getMessage(config.getString("SETTINGS.serverLanguage", "en_us"), "RULE_ADDED"));
            }
        }
        return true;
    }
}