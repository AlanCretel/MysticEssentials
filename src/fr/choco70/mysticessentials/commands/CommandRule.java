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

public class CommandRule implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private rulesManager rulesManager = plugin.getRulesManager();
    private playersManager playersManager = plugin.getPlayersManager();
    private langsManager langsManager = plugin.getLangsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        FileConfiguration config = plugin.getConfig();
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
            if(arguments.length == 0){
                player.sendMessage(command.getUsage());
            }
            else{
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
                        String rulePrefix = "Rule " + ruleNumber + ": ";
                        player.sendMessage(rulePrefix + rulesManager.getRule(rule));
                    }
                }
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Integer ruleNumber){
        String ruleNumber_placeholder = "#rule#";
        return string.replaceAll(ruleNumber_placeholder, ruleNumber.toString());
    }
}
