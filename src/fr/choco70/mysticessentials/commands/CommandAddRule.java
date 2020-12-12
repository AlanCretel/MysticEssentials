package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.RulesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandAddRule implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private RulesManager rulesManager = plugin.getRulesManager();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

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
                player.sendMessage(localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "RULE_ADDED"));
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
                sender.sendMessage(localesManager.getMessage(localesManager.getServerLocale(), "RULE_ADDED"));
            }
        }
        return true;
    }
}