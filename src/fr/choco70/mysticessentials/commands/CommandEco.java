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

public class CommandEco implements CommandExecutor {

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final EconomyLink economyLink = plugin.getEconomyLink();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final FileConfiguration config = plugin.getConfig();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String senderLocal;
        double defaultBalance = config.getDouble("SETTINGS.default_money_balance", 0.0);
        if(sender instanceof Player){
            Player player = (Player) sender;
            senderLocal = sqLiteManager.getPlayerLocale(player.getUniqueId());
        }
        else{
            senderLocal = localesManager.getServerLocale();
        }

        if(args == null){
            sender.sendMessage(command.getUsage());
        }
        else if(args.length == 2){
            Player target = plugin.getServer().getPlayer(args[1]);
            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")){
                if(target != null){
                    String specifyAmount = localesManager.getMessage(senderLocal, "SPECIFY_AMOUNT_ERROR");
                    sender.sendMessage(specifyAmount);
                }
                else{
                    sender.sendMessage(command.getUsage());
                }
            }
            else if(args[0].equalsIgnoreCase("default")){
                if(target != null){
                    economyLink.setMoney(target, defaultBalance);
                }
                else{
                    String playerNotFound = localesManager.getMessage(senderLocal, "PLAYER_NOT_FOUND");
                    sender.sendMessage(formatString(playerNotFound, args[1]));
                }
            }
        }
        else if(args.length == 3){
            Player target = plugin.getServer().getPlayer(args[1]);
            if(target != null){
                try{
                    String targetLocale = sqLiteManager.getPlayerLocale(target.getUniqueId());
                    double amount = Double.parseDouble(args[2]);
                    if(args[0].equalsIgnoreCase("add")){
                        economyLink.addMoney(target, amount);
                        String addMoneySender = localesManager.getMessage(senderLocal, "ECO_ADD_SENDER");
                        String addMoneyTarget = localesManager.getMessage(targetLocale, "ECO_ADD_TARGET");
                        sender.sendMessage(formatString(addMoneySender, sender, target, amount));
                        target.sendMessage(formatString(addMoneyTarget, sender, target, amount));
                    }
                    else if(args[0].equalsIgnoreCase("remove")){
                        economyLink.removeMoney(target, amount);
                        String removeMoneySender = localesManager.getMessage(senderLocal, "ECO_REMOVE_SENDER");
                        String removeMoneyTarget = localesManager.getMessage(targetLocale, "ECO_REMOVE_TARGET");
                        sender.sendMessage(formatString(removeMoneySender, sender, target, amount));
                        target.sendMessage(formatString(removeMoneyTarget, sender, target, amount));
                    }
                    else if(args[0].equalsIgnoreCase("set")){
                        economyLink.setMoney(target, amount);
                        String setMoneySender = localesManager.getMessage(senderLocal, "ECO_SET_SENDER");
                        String setMoneyTarget = localesManager.getMessage(targetLocale, "ECO_SET_TARGET");
                        sender.sendMessage(formatString(setMoneySender, sender, target, amount));
                        target.sendMessage(formatString(setMoneyTarget, sender, target, amount));
                    }
                } catch(NumberFormatException e){
                    String notValidNumber = localesManager.getMessage(senderLocal, "NOT_VALID_NUMBER");
                    sender.sendMessage(formatString(notValidNumber, args[2]));
                }
            }
            else{
                String playerNotFound = localesManager.getMessage(senderLocal, "PLAYER_NOT_FOUND");
                sender.sendMessage(formatString(playerNotFound, args[1]));
            }
        }
        else{
            sender.sendMessage(command.getUsage());
        }

        return true;
    }

    private String formatString(String message, String sub) {
        String targetPlaceholder = "#target#";
        String argPlaceholder = "#arg#";
        return message.replaceAll(targetPlaceholder, sub).replaceAll(argPlaceholder, sub);
    }

    private String formatString(String message, CommandSender sender, Player target, double amount){
        String senderPlaceholder = "#sender#";
        String targetPlaceholder = "#target#";
        String amountPlaceholder = "#amount#";
        String symbol_placeholder = "#symbol#";

        String currencySymbol = config.getString("\\" + "SETTINGS.currencySymbol", "\\$");
        String senderName = "";

        if(sender instanceof Player){
            senderName = ((Player)sender).getName();
        }
        else{
            senderName = "Server";
        }

        return message.replaceAll(senderPlaceholder, senderName).replaceAll(targetPlaceholder, target.getName()).replaceAll(amountPlaceholder, "" + amount).replaceAll(symbol_placeholder, currencySymbol);
    }
}
