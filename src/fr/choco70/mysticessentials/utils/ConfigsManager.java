package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ConfigsManager{

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    public void transferConfig(){
        // transfer players data
        File playersFolder = new File(plugin.getDataFolder() + File.separator + "players");
        if(playersFolder.exists() && playersFolder.isDirectory()){
            try{
                File[] files = playersFolder.listFiles();
                String defaultLocale = localesManager.getServerLocale();
                if(files != null){
                    for (File file : files) {
                        try{
                            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                            String playerLocale = playerConfig.getString("language", defaultLocale);
                            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(file.getName().replaceAll("\\.yml", "")));
                            String displayName = playerConfig.getString("display_name", player.getName());
                            sqLiteManager.insertPlayer(player.getUniqueId(), player.getName(), displayName, playerLocale);
                            if(playerConfig.isConfigurationSection("last_location")){
                                String world = playerConfig.getString("last_location.world");
                                double x = playerConfig.getDouble("last_location.x");
                                double y = playerConfig.getDouble("last_location.y");
                                double z = playerConfig.getDouble("last_location.z");
                                float pitch = Float.parseFloat(playerConfig.getString("last_location.pitch"));
                                float yaw = Float.parseFloat(playerConfig.getString("last_location.yaw"));

                                Location lastLocation = new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);

                                if(sqLiteManager.getLastLocation(player.getUniqueId()) != null){
                                    sqLiteManager.updateLastLocation(player.getUniqueId(), lastLocation);
                                }
                                else{
                                    sqLiteManager.setLastLocation(player.getUniqueId(), lastLocation);
                                }
                            }
                            if(playerConfig.isConfigurationSection("homes")){
                                for (String home : playerConfig.getConfigurationSection("homes").getKeys(false)) {
                                    String world = playerConfig.getString("homes." + home + ".world");
                                    double x = playerConfig.getDouble("homes." + home + ".x");
                                    double y = playerConfig.getDouble("homes." + home + ".y");
                                    double z = playerConfig.getDouble("homes." + home + ".z");
                                    float pitch = Float.parseFloat(playerConfig.getString("homes." + home + ".pitch"));
                                    float yaw = Float.parseFloat(playerConfig.getString("homes." + home + ".yaw"));
                                    boolean isDefault = playerConfig.getBoolean("homes." + home + ".default");
                                    Location location = new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
                                    if(sqLiteManager.haveHome(player.getUniqueId(), home)){
                                        sqLiteManager.updateHome(player.getUniqueId(), home, isDefault, location);
                                    }
                                    else{
                                        sqLiteManager.insertHome(player.getUniqueId(), home, isDefault, location);
                                    }
                                }
                            }
                            if(playerConfig.isConfigurationSection("last_death")){
                                String world = playerConfig.getString("last_death.world");
                                double x = playerConfig.getDouble("last_death.x");
                                double y = playerConfig.getDouble("last_death.y");
                                double z = playerConfig.getDouble("last_death.z");
                                float pitch = Float.parseFloat(playerConfig.getString("last_death.pitch"));
                                float yaw = Float.parseFloat(playerConfig.getString("last_death.yaw"));

                                Location lastDeath = new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);

                                if(sqLiteManager.getLastDeath(player.getUniqueId()) != null){
                                    sqLiteManager.updateLastDeath(player.getUniqueId(), lastDeath);
                                }
                                else{
                                    sqLiteManager.setLastDeath(player.getUniqueId(), lastDeath);
                                }
                            }
                            List<String> ignored_players = playerConfig.getStringList("ignored_players");
                            for (String ignored_player : ignored_players) {
                                UUID uuid = UUID.fromString(ignored_player);
                                if(!sqLiteManager.doesIgnorePlayer(player.getUniqueId(), uuid)){
                                    sqLiteManager.setIgnorePlayer(player.getUniqueId(), uuid);
                                }
                            }
                            file.delete();
                        }
                        catch(Exception ignored){}
                    }
                }
                playersFolder.delete();
            }
            catch(Exception ignored){

            }
        }
        FileConfiguration config = plugin.getConfig();
        if(config.isConfigurationSection("WARPS")){
            for (String warp : config.getConfigurationSection("WARPS").getKeys(false)) {
                String world = config.getString("WARPS." + warp + ".world");
                double x = config.getDouble("WARPS." + warp + ".x");
                double y = config.getDouble("WARPS." + warp + ".y");
                double z = config.getDouble("WARPS." + warp + ".z");
                float pitch = Float.parseFloat(config.getString("WARPS." + warp + ".pitch"));
                float yaw = Float.parseFloat(config.getString("WARPS." + warp + ".yaw"));

                Location location = new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
                sqLiteManager.insertWarp(warp, location);
            }
            config.set("WARPS", null);
        }
        plugin.saveConfig();
    }

}
