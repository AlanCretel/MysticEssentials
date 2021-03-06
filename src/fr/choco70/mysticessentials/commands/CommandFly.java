package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(arguments.length >= 1 && player.hasPermission("mysticessentials.fly.others")){
                Player target = player.getServer().getPlayer(arguments[0]);
                if(target != null){
                    if(target.getAllowFlight()){
                        target.setAllowFlight(false);
                        String unableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(target.getUniqueId()), "UNABLE_TO_FLY");
                        String unallowedToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "UNALLOWED_TO_FLY");
                        target.sendMessage(unableToFly);
                        player.sendMessage(formatString(unallowedToFly, target.getName()));
                    }
                    else{
                        target.setAllowFlight(true);
                        String ableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(target.getUniqueId()), "ABLE_TO_FLY");
                        String allowedToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "ALLOWED_TO_FLY");
                        target.sendMessage(ableToFly);
                        player.sendMessage(formatString(allowedToFly, target.getName()));
                    }
                }
                else{
                    String playerNotFound = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "PLAYER_NOT_FOUND");
                    player.sendMessage(formatString(playerNotFound, arguments[0]));
                }
            }
            else{
                if(player.getAllowFlight()){
                    player.setAllowFlight(false);
                    String unableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "UNABLE_TO_FLY");
                    player.sendMessage(unableToFly);
                }
                else{
                    player.setAllowFlight(true);
                    String ableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(player.getUniqueId()), "ABLE_TO_FLY");
                    player.sendMessage(ableToFly);
                }
            }
        }
        else{
            if(arguments.length >= 1){
                Player target = sender.getServer().getPlayer(arguments[0]);
                if(target != null){
                    if(target.getAllowFlight()){
                        target.setAllowFlight(false);
                        String unableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(target.getUniqueId()), "UNABLE_TO_FLY");
                        String unallowedToFly = localesManager.getMessage(serverLanguage, "UNALLOWED_TO_FLY");
                        target.sendMessage(unableToFly);
                        sender.sendMessage(formatString(unallowedToFly, target.getName()));
                    }
                    else{
                        target.setAllowFlight(true);
                        String ableToFly = localesManager.getMessage(sqLiteManager.getPlayerLocale(target.getUniqueId()), "ABLE_TO_FLY");
                        String allowedToFly = localesManager.getMessage(serverLanguage, "ALLOWED_TO_FLY");
                        target.sendMessage(ableToFly);
                        sender.sendMessage(formatString(allowedToFly, target.getName()));
                    }
                }
                else{
                    String playerNotFound = localesManager.getMessage(serverLanguage, "PLAYER_NOT_FOUND");
                    sender.sendMessage(formatString(playerNotFound, arguments[0]));
                }
            }
            else{
                sender.sendMessage(command.getUsage());
            }
        }
        return true;
    }

    public String formatString(String string, String targetName){
        String target_placeholder = "#target#";
        return string.replaceAll(target_placeholder, targetName);
    }
}
