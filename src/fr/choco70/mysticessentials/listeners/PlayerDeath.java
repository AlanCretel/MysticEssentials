package fr.choco70.mysticessentials.listeners;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private LocalesManager localesManager = plugin.getLocalesManager();
    private SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        Location deathLocation = player.getLocation().clone();

        if(config.getBoolean("SETTINGS.showDeathCoordinates", true)){
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            String deathLocationMessage = localesManager.getMessage(playerLanguage, "DEATH_LOCATION_MESSAGE");
            player.sendMessage(formatString(deathLocationMessage, player));
        }
        if(sqLiteManager.haveLastLocation(player.getUniqueId())){
            sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
        }
        else{
            sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
        }
        if(sqLiteManager.getLastDeath(player.getUniqueId()) == null){
            sqLiteManager.setLastDeath(player.getUniqueId(), deathLocation);
        }
        else{
            sqLiteManager.updateLastDeath(player.getUniqueId(), deathLocation);
        }
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
