package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());

            if(sqLiteManager.haveLastLocation(player.getUniqueId())){
                String teleportToLastLocationMessage = localesManager.getMessage(playerLanguage, "TELEPORT_LAST_LOCATION");

                player.sendMessage(teleportToLastLocationMessage);
                Location lastLocation = sqLiteManager.getLastLocation(player.getUniqueId());
                player.teleport(lastLocation);
                if(sqLiteManager.haveLastLocation(player.getUniqueId())){
                    sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
                }
                else{
                    sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
                }
            }
            else{
                String noLastLocationMessage = localesManager.getMessage(playerLanguage, "NO_LAST_LOCATION");
                player.sendMessage(noLastLocationMessage);
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(localesManager.getServerLocale(), "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
