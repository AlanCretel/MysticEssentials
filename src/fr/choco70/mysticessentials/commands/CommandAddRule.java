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

public class CommandAddRule implements CommandExecutor{

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
                player.sendMessage(langsManager.getMessage(playersManager.getPlayerLanguage(player), "RULE_ADDED"));
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
                sender.sendMessage(langsManager.getMessage(config.getString("SETTINGS.serverLanguage", "en_us"), "RULE_ADDED"));
            }
        }
        return true;
    }
}