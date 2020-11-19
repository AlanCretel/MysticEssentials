package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.EconomyLink;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandBalance implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final PlayersManager playersManager = plugin.getPlayersManager();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final EconomyLink economyLink = plugin.getEconomyLink();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null){
                    String targetBalanceMessage = localesManager.getMessage(playersManager.getPlayerLanguage(player), "BALANCE_OTHER");
                    Double targetBalance = economyLink.getPlayerBalance(target);
                    player.sendMessage(formatString(targetBalanceMessage, target.getName(), targetBalance));
                }
                else{
                    String playerNotFound = localesManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_NOT_FOUND");
                    player.sendMessage(playerNotFound);
                }
            }
            else{
                Double playerBalance = economyLink.getPlayerBalance(player);
                String balanceMessage = localesManager.getMessage(playersManager.getPlayerLanguage(player), "BALANCE_SELF");
                player.sendMessage(formatString(balanceMessage, null, playerBalance));
            }
        }
        else{
            if(arguments.length == 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null){
                    String targetBalanceMessage = localesManager.getMessage(serverLanguage, "BALANCE_OTHER");
                    Double targetBalance = economyLink.getPlayerBalance(target);
                    sender.sendMessage(formatString(targetBalanceMessage, target.getName(), targetBalance));
                }
                else{
                    String playerNotFound = localesManager.getMessage(serverLanguage, "PLAYER_NOT_FOUND");
                    sender.sendMessage(playerNotFound);
                }
            }
            else{
                sender.sendMessage(command.getUsage());
            }
        }
        return true;
    }

    public String formatString(String string, String target, Double balance){
        String target_placeholder = "#target#";
        String balance_placeholder = "#balance#";
        String symbol_placeholder = "#symbol#";

        String currencySymbol = config.getString("\\" + "SETTINGS.currencySymbol", "\\$");

        String formatedString = string.replaceAll(balance_placeholder, balance.toString());

        if(target == null){
            formatedString = formatedString.replaceAll(symbol_placeholder, currencySymbol);
            return formatedString;
        }
        else{
            formatedString = formatedString.replaceAll(symbol_placeholder, currencySymbol);
            formatedString = formatedString.replaceAll(target_placeholder, target);
            return formatedString;
        }
    }
}
