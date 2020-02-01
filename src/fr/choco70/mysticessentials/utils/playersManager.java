package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        playerConfig.set("homes." + homeName + ".default", false);
        savePlayerConfig(player, playerConfig);
    }

    public String getPlayerLanguage(Player player){
        String serverLanguage = plugin.getConfig().getString("SETTINGS.serverLanguage", "en_us");
        return getPlayerConfig(player).getString("language", serverLanguage);
    }

    public void setLastLocation(Player player){
        FileConfiguration playerConfig = getPlayerConfig(player);
        playerConfig.set("last_location.world", player.getLocation().getWorld().getName());
        playerConfig.set("last_location.x", player.getLocation().getX());
        playerConfig.set("last_location.y", player.getLocation().getY());
        playerConfig.set("last_location.z", player.getLocation().getZ());
        playerConfig.set("last_location.pitch", player.getLocation().getPitch());
        playerConfig.set("last_location.yaw", player.getLocation().getYaw());
        savePlayerConfig(player, playerConfig);
    }

    public Integer getHomesLimit(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> homesLimits = new ArrayList<>();
        homesLimits.add(plugin.getConfig().getInt("SETTINGS.default_homes_limit", 1));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.homeslimit.")){
                String limit = permission.getPermission().replace("mysticessentials.homeslimit.", "");
                if(limit.equalsIgnoreCase("nolimit")){
                    return 1000;
                }
                else{
                    homesLimits.add(Integer.parseInt(limit));
                }
            }
        }
        return Collections.max(homesLimits);
    }

    public Integer getHomesNumber(Player player){
        if(getPlayerConfig(player).isConfigurationSection("homes")){
            return getHomeList(player).size();
        }
        else{
            return 0;
        }
    }

    public Set<String> getHomeList(Player player){
        if(getPlayerConfig(player).isConfigurationSection("homes")){
            return getPlayerConfig(player).getConfigurationSection("homes").getKeys(false);
        }
        else{
            return null;
        }
    }

    public String getDefaultHome(Player player){
        Set<String> homeList = getHomeList(player);
        FileConfiguration playerConfig = getPlayerConfig(player);
        for (String s : homeList) {
            if(playerConfig.getBoolean("homes." + s + ".default", false)){
                return s;
            }
        }
        if(homeList.contains("home")){
            return "home";
        }
        else{
            return null;
        }
    }

    public void setDefaultHome(Player player, String homeName){
        FileConfiguration playerConfig = getPlayerConfig(player);
        if(getDefaultHome(player) == null){
            playerConfig.set("homes." + homeName + ".default", true);
            savePlayerConfig(player, playerConfig);
        }
        else{
            playerConfig.set("homes." + getDefaultHome(player) + ".default", false);
            playerConfig.set("homes." + homeName + ".default", true);
            savePlayerConfig(player, playerConfig);
        }
    }

    public void addIgnoredPlayer(Player player, Player ignored){
        List<String> ignoredPlayers = getIgnoredPlayers(player);
        if(ignoredPlayers == null){
            List<String> ignoredPlayer = new ArrayList<>();
            ignoredPlayer.add(ignored.getUniqueId().toString());
            FileConfiguration playerConfig = getPlayerConfig(player);
            playerConfig.set("ignored_players", ignoredPlayer);
            savePlayerConfig(player, playerConfig);
        }
        else{
            if(!ignoredPlayers.contains(ignored.getUniqueId().toString())){
                FileConfiguration playerConfig = getPlayerConfig(player);
                ignoredPlayers.add(ignored.getUniqueId().toString());
                playerConfig.set("ignored_players", ignoredPlayers);
                savePlayerConfig(player, playerConfig);
            }
        }
    }

    public void delIgnoredPlayer(Player player, Player ignored){
        List<UUID> ignoredPlayers = getPlayersIgnored(player);
        if(ignoredPlayers != null && ignoredPlayers.contains(ignored.getUniqueId())){
            FileConfiguration playerConfig = getPlayerConfig(player);
            ignoredPlayers.remove(ignored.getUniqueId());
            playerConfig.set("ignored_players", ignoredPlayers);
            savePlayerConfig(player, playerConfig);
        }
        else{
            if(ignoredPlayers != null){
                FileConfiguration playerConfig = getPlayerConfig(player);
                ignoredPlayers.remove(ignored.getUniqueId());
                playerConfig.set("ignored_players", ignoredPlayers);
                savePlayerConfig(player, playerConfig);
            }
        }
    }

    public List<String> getIgnoredPlayers(Player player){
        List<?> ignoredList = getPlayerConfig(player).getList("ignored_players");
        if(ignoredList == null){
            return null;
        }
        else{
            List<String> ignoredPlayers = new ArrayList<>();
            for (Object o : ignoredList) {
                ignoredPlayers.add(plugin.getServer().getOfflinePlayer(UUID.fromString(o.toString())).getName());
            }
            return ignoredPlayers;
        }
    }

    public List<UUID> getPlayersIgnored(Player player){
        List<?> ignoredList = getPlayerConfig(player).getList("ignored_players");
        if(ignoredList == null){
            return null;
        }
        else{
            List<UUID> playersUUID = new ArrayList<>();
            for (Object o : ignoredList) {
                playersUUID.add(UUID.fromString(o.toString()));
            }
            return playersUUID;
        }
    }

    public List<String> toUserNames(List<UUID> userUUIDS){
        List<String> playerNames = new ArrayList<>();
        for (UUID userUUID : userUUIDS){
            playerNames.add(plugin.getServer().getOfflinePlayer(userUUID).getName());
        }
        return playerNames;
    }
}
