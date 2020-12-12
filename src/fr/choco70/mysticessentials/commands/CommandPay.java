package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.EconomyLink;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandPay implements CommandExecutor {

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final EconomyLink economyLink = plugin.getEconomyLink();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();
    private final FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            if(args == null){
                sender.sendMessage(command.getUsage());
            }
            else if(args.length != 2){
                sender.sendMessage(command.getUsage());
            }
            else{
                Player player = (Player) sender;
                Player target = plugin.getServer().getPlayer(args[0]);
                String playerLocale = sqLiteManager.getPlayerLocale(player.getUniqueId());
                if(target == null){
                    String playerNotFound = localesManager.getMessage(playerLocale, "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, args[0], 0.0));
                }
                else if(target == player){
                    String selfPay = localesManager.getMessage(playerLocale, "SELF_PAY");
                    player.sendMessage(selfPay);
                }
                else{
                    try{
                        double amount = Double.parseDouble(args[1]);
                        if(economyLink.getPlayerBalance(player) >= amount){
                            if(amount <= 0){
                                String positiveNumbersMessage = localesManager.getMessage(playerLocale, "POSITIVE_VALUES_ONLY");
                                player.sendMessage(positiveNumbersMessage);
                            }
                            else{
                                economyLink.transferMoney(player, target, amount);
                                String playerMessage = localesManager.getMessage(playerLocale, "PAYED_SENDER");
                                String targetMessage = localesManager.getMessage(sqLiteManager.getPlayerLocale(target.getUniqueId()), "PAYED_TARGET");
                                player.sendMessage(formatString(playerMessage, target.getName(), amount));
                                target.sendMessage(formatString(targetMessage, player.getName(), amount));
                            }
                        }
                        else{
                            String notEnoughMoney = localesManager.getMessage(playerLocale, "NOT_ENOUGH_MONEY");
                            player.sendMessage(formatString(notEnoughMoney, target.getName(), amount));
                        }
                    }
                    catch (NumberFormatException e){
                        player.sendMessage(formatString(localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "NOT_VALID_NUMBER"), null, args[1]));
                    }
                }
            }
        }
        else{
            sender.sendMessage(localesManager.getMessage(localesManager.getServerLocale(), "ONLY_PLAYERS_COMMAND"));
        }
        return true;
    }

    public String formatString(String string, String target, Double amount){
        String target_placeholder = "#target#";
        String balance_placeholder = "#amount#";
        String symbol_placeholder = "#symbol#";

        String currencySymbol = config.getString("\\" + "SETTINGS.currencySymbol", "\\$");
        String formatedString = string;

        if(target == null){
            formatedString = formatedString.replaceAll(symbol_placeholder, currencySymbol);
            return formatedString;
        }
        else if(amount == null){
            formatedString = formatedString.replaceAll(target_placeholder, target);
            return formatedString;
        }
        else{
            formatedString = formatedString.replaceAll(balance_placeholder, amount.toString());
            formatedString = formatedString.replaceAll(symbol_placeholder, currencySymbol);
            formatedString = formatedString.replaceAll(target_placeholder, target);
            return formatedString;
        }
    }

    public String formatString(String string, String target, String arg){
        String target_placeholder = "#target#";
        String arg_placeholder = "#arg#";

        String formatedString = string;

        if (arg != null) {
            formatedString = formatedString.replaceAll(arg_placeholder, arg);
        }
        formatedString = formatedString.replaceAll(target_placeholder, target);
        return formatedString;
    }
}