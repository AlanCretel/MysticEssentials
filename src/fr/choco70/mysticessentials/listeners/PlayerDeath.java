package fr.choco70.mysticessentials.listeners;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        Location deathLocation = player.getLocation().clone();
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(playerUUID));

        if(config.getBoolean("SETTINGS.showDeathCoordinates", true)){
            FileConfiguration playerLanguageConfig = YamlConfiguration.loadConfiguration(plugin.getLanguageFile(playerConfig.getString("language", "en_us")));

            String deathLocationMessage = playerLanguageConfig.getString("DEATH_LOCATION_MESSAGE", "Death location: /nWorld: #world#/nX: #x#/nY: #y#/nZ: #z#");
            playerLanguageConfig.set("DEATH_LOCATION_MESSAGE", deathLocationMessage);

            plugin.saveLanguageConfig(playerLanguageConfig, playerConfig.getString("language", "en_us"));

            player.sendMessage(formatString(deathLocationMessage, player));
        }
        playerConfig.set("last_death.world", deathLocation.getWorld().getName());
        playerConfig.set("last_death.x", deathLocation.getX());
        playerConfig.set("last_death.y", deathLocation.getY());
        playerConfig.set("last_death.z", deathLocation.getZ());
        playerConfig.set("last_death.pitch", deathLocation.getPitch());
        playerConfig.set("last_death.yaw", deathLocation.getYaw());
        playerConfig.set("last_location.world", deathLocation.getWorld().getName());
        playerConfig.set("last_location.x", deathLocation.getX());
        playerConfig.set("last_location.y", deathLocation.getY());
        playerConfig.set("last_location.z", deathLocation.getZ());
        playerConfig.set("last_location.pitch", deathLocation.getPitch());
        playerConfig.set("last_location.yaw", deathLocation.getYaw());

        plugin.savePlayerConfig(playerConfig, playerUUID);
    }

    public String formatString(String string, Player player){
        String x_placeholder = "#x#";
        String y_placeholder = "#y#";
        String z_placeholder = "#z#";
        String world_placeholder = "#world#";
        String player_placeholder = "#player#";
        String newline_placeholder = "/n";

        String formatedString = string.replaceAll(x_placeholder, String.valueOf(Math.floor(player.getLocation().getX())));
        formatedString = formatedString.replaceAll(y_placeholder, String.valueOf(Math.floor(player.getLocation().getY())));
        formatedString = formatedString.replaceAll(z_placeholder, String.valueOf(Math.floor(player.getLocation().getZ())));
        formatedString = formatedString.replaceAll(world_placeholder, player.getLocation().getWorld().getName());
        formatedString = formatedString.replaceAll(player_placeholder, player.getName());
        formatedString = formatedString.replaceAll(newline_placeholder, "\n");

        return formatedString;
    }

}