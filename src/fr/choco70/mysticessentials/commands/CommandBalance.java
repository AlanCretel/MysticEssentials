package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CommandBalance implements CommandExecutor{

    private static Economy economy = null;
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private playersManager playersManager = new playersManager();
    private langsManager langsManager = new langsManager();

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");
        if(setupEconomy()){
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(arguments.length == 1){
                    Player target = sender.getServer().getPlayer(arguments[0]);
                    if(target != null){
                        String targetBalanceMessage = langsManager.getMessage(playersManager.getPlayerLanguage(player), "BALANCE_OTHER", "#target#'s balance: #balance##symbol#");
                        Double targetBalance = economy.getBalance(target);
                        player.sendMessage(formatString(targetBalanceMessage, target.getName(), targetBalance));
                    }
                    else{
                        String playerNotFound = langsManager.getMessage(playersManager.getPlayerLanguage(player), "PLAYER_NOT_FOUND", "Player #target# was not found.");
                        player.sendMessage(playerNotFound);
                    }
                }
                else{
                    Double playerBalance = economy.getBalance(player);
                    String balanceMessage = langsManager.getMessage(playersManager.getPlayerLanguage(player), "BALANCE_SELF", "Your balance: #balance##symbol#");
                    player.sendMessage(formatString(balanceMessage, null, playerBalance));
                }
            }
            else{
                if(arguments.length == 1){
                    Player target = sender.getServer().getPlayer(arguments[0]);
                    if(target != null){
                        String targetBalanceMessage = langsManager.getMessage(serverLanguage, "BALANCE_OTHER", "#target#'s balance: #balance##symbol#");
                        Double targetBalance = economy.getBalance(target);
                        sender.sendMessage(formatString(targetBalanceMessage, target.getName(), targetBalance));
                    }
                    else{
                        String playerNotFound = langsManager.getMessage(serverLanguage, "PLAYER_NOT_FOUND", "Player #target# was not found.");
                        sender.sendMessage(playerNotFound);
                    }
                }
                else{
                    sender.sendMessage(command.getUsage());
                }
            }
        }
        else{
            if(sender instanceof Player){
                Player player = (Player)sender;
                String noEconomy = langsManager.getMessage(playersManager.getPlayerLanguage(player), "NO_ECONOMY", "No economy present on this server.");
                player.sendMessage(noEconomy);
            }
            else{
                String noEconomy = langsManager.getMessage(serverLanguage, "NO_ECONOMY", "No economy present on this server.");
                sender.sendMessage(noEconomy);
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
