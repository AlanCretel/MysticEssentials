package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import fr.choco70.mysticessentials.utils.rulesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandDelRule implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private rulesManager rulesManager = plugin.getRulesManager();
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        if(sender instanceof Player){
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                Integer rule = Integer.parseInt(arguments[0]) - 1;
                Integer ruleNumber = rule + 1;
                if(rulesManager.getRulesList().size() == 0 || (rulesManager.getRulesList().size() == 1 && rulesManager.getRule(0).equals(""))){
                    player.sendMessage(langsManager.getMessage(playerLanguage, "NO_RULES", "No rules were set."));
                }
                else{
                    if(ruleNumber > rulesManager.getRulesNumber() || ruleNumber <= 0){
                        player.sendMessage(formatString(langsManager.getMessage(playerLanguage, "RULE_NOT_EXIST", "This rule does not exist."), ruleNumber));
                    }
                    else{
                        rulesManager.removeRule(rule);
                        player.sendMessage(formatString(langsManager.getMessage(playerLanguage, "RULE_REMOVED", "Rule #rule# successfully removed."), ruleNumber));
                    }
                }
            }
        }
        else{
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else{
                String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
                Integer rule = Integer.parseInt(arguments[0]) - 1;
                Integer ruleNumber = rule + 1;
                if(rulesManager.getRulesList().size() == 0 || (rulesManager.getRulesList().size() == 1 && rulesManager.getRule(0).equals(""))){
                    sender.sendMessage(langsManager.getMessage(serverLanguage, "NO_RULES", "No rules were set."));
                }
                else{
                    if(ruleNumber > rulesManager.getRulesNumber() || ruleNumber <= 0){
                        sender.sendMessage(formatString(langsManager.getMessage(serverLanguage, "RULE_NOT_EXIST", "This rule does not exist."), ruleNumber));
                    }
                    else{
                        rulesManager.removeRule(rule);
                        sender.sendMessage(formatString(langsManager.getMessage(serverLanguage, "RULE_REMOVED", "Rule #rule# successfully removed."), ruleNumber));
                    }
                }
            }
        }
        return true;
    }

    public String formatString(String string, Integer ruleNumber){
        String ruleNumber_placeholder = "#rule#";
        return string.replaceAll(ruleNumber_placeholder, ruleNumber.toString());
    }
}
