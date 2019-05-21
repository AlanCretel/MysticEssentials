package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class playersManager{
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private File dataFolder = plugin.getDataFolder();

    public void createPlayerFile(Player player){
        String fileName = player.getUniqueId().toString();
        File playersForlder = new File(dataFolder + File.separator + "players" + File.separator);
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }
        if(!playersForlder.exists()){
            playersForlder.mkdir();
        }
        File file = new File(playersForlder,fileName + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getPlayerFile(Player player){
        File playersForlder = new File(dataFolder + File.separator + "players" + File.separator);
        return new File(playersForlder,player.getUniqueId().toString() + ".yml");
    }

    public FileConfiguration getPlayerConfig(Player player){
        return YamlConfiguration.loadConfiguration(getPlayerFile(player));
    }

    public void savePlayerConfig(Player player, FileConfiguration playerConfig){
        try {
            playerConfig.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHome(Player player, String homeName){
        FileConfiguration playerConfig = getPlayerConfig(player);
        Location playerLocation = player.getLocation();
        playerConfig.set("homes." + homeName + ".world", player.getWorld().getName());
        playerConfig.set("homes." + homeName + ".x", playerLocation.getX());
        playerConfig.set("homes." + homeName + ".y", playerLocation.getY());
        playerConfig.set("homes." + homeName + ".z", playerLocation.getZ());
        playerConfig.set("homes." + homeName + ".pitch", playerLocation.getPitch());
        playerConfig.set("homes." + homeName + ".yaw", playerLocation.getYaw());
        savePlayerConfig(player, playerConfig);
    }

    public String getPlayerLanguage(Player player){
        String serverLanguage = plugin.getConfig().getString("SETTINGS.serverLanguage", "en_us");
        return getPlayerConfig(player).getString("language", serverLanguage);
    }

    public void setLastLocation(Player player){
        FileConfiguration playerConfig = getPlayerConfig(player);
        playerConfig.set("lastlocation.world", player.getLocation().getWorld().getName());
        playerConfig.set("lastlocation.x", player.getLocation().getX());
        playerConfig.set("lastlocation.y", player.getLocation().getY());
        playerConfig.set("lastlocation.z", player.getLocation().getZ());
        playerConfig.set("lastlocation.pitch", player.getLocation().getPitch());
        playerConfig.set("lastlocation.yaw", player.getLocation().getYaw());
        savePlayerConfig(player, playerConfig);
    }
}
