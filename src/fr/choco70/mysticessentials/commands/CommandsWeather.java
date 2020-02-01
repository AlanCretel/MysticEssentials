package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import fr.choco70.mysticessentials.utils.playersManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsWeather implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private langsManager langsManager = plugin.getLangsManager();
    private playersManager playersManager = plugin.getPlayersManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String serverLanguage = langsManager.getServerLanguage();
        boolean changeAllWorldsWeather = plugin.getConfig().getBoolean("SETTINGS.change_all_worlds_weather", true);
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = playersManager.getPlayerLanguage(player);
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
                player.sendMessage(langsManager.getMessage(playerLanguage, "SET_WEATHER_SUN"));
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
                player.sendMessage(langsManager.getMessage(playerLanguage, "SET_WEATHER_RAIN"));
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
                player.sendMessage(langsManager.getMessage(playerLanguage, "SET_WEATHER_STORM"));
            }
        }
        else{
            sender.sendMessage(langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND"));
        }
        return true;
    }
}
