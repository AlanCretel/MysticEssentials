package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.RulesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetRule implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final RulesManager rulesManager = plugin.getRulesManager();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            if(arguments.length == 0){
                sender.sendMessage(command.getUsage());
            }
            else if(arguments.length == 1){
                sender.sendMessage(command.getUsage());
            }
            else{
                Player player = (Player)sender;
                String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
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
            String serverLanguage = localesManager.getServerLocale();
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