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

public class CommandSetRule implements CommandExecutor{

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
            else if(arguments.length == 1){
                sender.sendMessage(command.getUsage());
            }
            else{
                Player player = (Player)sender;
                String playerLanguage = playersManager.getPlayerLanguage(player);
                Integer rule = Integer.parseInt(arguments[0]) - 1;
                Integer ruleNumber = rule + 1;
                if(rulesManager.getRulesList().size() == 0 || (rulesManager.getRulesList().size() == 1 && rulesManager.getRule(0).equals(""))){
                    player.sendMessage(localesManager.getMessage(playerLanguage, "NO_RULES"));
                }
                else{
                    if(ruleNumber > rulesManager.getRulesNumber() || ruleNumber <= 0){
                        player.sendMessage(formatString(localesManager.getMessage(playerLanguage, "RULE_NOT_EXIST"), ruleNumber));
                    }
                    else{
                        String newRule = "";
                        for (int i = 0; i < arguments.length; i++) {
                            if(i != 0){
                                newRule = newRule + arguments[i] + " ";
                            }
                        }
                        rulesManager.setRule(rule, newRule);
                        player.sendMessage(formatString(localesManager.getMessage(playerLanguage, "RULE_MODIFIED"), ruleNumber));
                    }
                }
            }
        }
        else{
            String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else if(arguments.length == 1){
                sender.sendMessage(command.getUsage());
            }
            else{
                Integer rule = Integer.parseInt(arguments[0]) - 1;
                Integer ruleNumber = rule + 1;
                if(rulesManager.getRulesList().size() == 0 || (rulesManager.getRulesList().size() == 1 && rulesManager.getRule(0).equals(""))){
                    sender.sendMessage(localesManager.getMessage(serverLanguage, "NO_RULES"));
                }
                else{
                    if(ruleNumber > rulesManager.getRulesNumber() || ruleNumber <= 0){
                        sender.sendMessage(formatString(localesManager.getMessage(serverLanguage, "RULE_NOT_EXIST"), ruleNumber));
                    }
                    else{
                        String newRule = "";
                        for (int i = 0; i < arguments.length; i++) {
                            if(i != 0){
                                newRule = newRule + arguments[i] + " ";
                            }
                        }
                        rulesManager.setRule(rule, newRule);
                        sender.sendMessage(formatString(localesManager.getMessage(serverLanguage, "RULE_MODIFIED"), ruleNumber));
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