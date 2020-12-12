package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandsTime implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        FileConfiguration config = plugin.getConfig();

        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            boolean changeAllWorldsTime = config.getBoolean("SETTINGS.change_all_worlds_time", true);

            if(command.getName().equalsIgnoreCase("day")){
                if(changeAllWorldsTime){
                    for (World world : player.getServer().getWorlds()){
                        world.setTime(0);
                    }
                }
                else{
                    player.getWorld().setTime(0);
                }
                player.sendMessage(localesManager.getMessage(playerLanguage, "SET_TIME_DAY"));
            }
            else if(command.getName().equalsIgnoreCase("night")){
                if(changeAllWorldsTime){
                    for (World world : player.getServer().getWorlds()){
                        world.setTime(14000);
                    }
                }
                else{
                    player.getWorld().setTime(14000);
                }
                player.sendMessage(localesManager.getMessage(playerLanguage, "SET_TIME_NIGHT"));
            }
        }
        else{
            sender.sendMessage(localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND"));
        }
        return true;
    }
}
