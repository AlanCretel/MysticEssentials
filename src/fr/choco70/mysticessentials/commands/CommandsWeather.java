package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsWeather implements CommandExecutor{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        boolean changeAllWorldsWeather = plugin.getConfig().getBoolean("SETTINGS.change_all_worlds_weather", true);
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            int newWeatherDuration = plugin.getConfig().getInt("SETTINGS.new_weather_duration", 10000);
            if(command.getName().equalsIgnoreCase("sun")){
                if(changeAllWorldsWeather){
                    for (World world : player.getServer().getWorlds()) {
                        world.setThundering(false);
                        world.setStorm(false);
                        world.setWeatherDuration(newWeatherDuration);
                    }
                }
                else{
                    player.getWorld().setStorm(false);
                    player.getWorld().setThundering(false);
                    player.getWorld().setWeatherDuration(newWeatherDuration);
                }
                player.sendMessage(localesManager.getMessage(playerLanguage, "SET_WEATHER_SUN"));
            }
            else if(command.getName().equalsIgnoreCase("rain")){
                if(changeAllWorldsWeather){
                    for (World world : player.getServer().getWorlds()) {
                        world.setThundering(false);
                        world.setStorm(true);
                        world.setWeatherDuration(newWeatherDuration);
                    }
                }
                else{
                    player.getWorld().setThundering(false);
                    player.getWorld().setStorm(true);
                    player.getWorld().setWeatherDuration(newWeatherDuration);
                }
                player.sendMessage(localesManager.getMessage(playerLanguage, "SET_WEATHER_RAIN"));
            }
            else if(command.getName().equalsIgnoreCase("storm")){
                if(changeAllWorldsWeather){
                    for (World world : player.getServer().getWorlds()) {
                        world.setStorm(true);
                        world.setThundering(true);
                        world.setWeatherDuration(newWeatherDuration);
                    }
                }
                else{
                    player.getWorld().setStorm(true);
                    player.getWorld().setThundering(true);
                    player.getWorld().setWeatherDuration(newWeatherDuration);
                }
                player.sendMessage(localesManager.getMessage(playerLanguage, "SET_WEATHER_STORM"));
            }
        }
        else{
            sender.sendMessage(localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND"));
        }
        return true;
    }
}
