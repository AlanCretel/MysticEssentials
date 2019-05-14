package fr.choco70.mysticessentials.listeners;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();

        if(config.getBoolean("SETTINGS.spawnOnDie", true) && config.getBoolean("SETTINGS.spawnOverrideBedSpawn", false) && config.isSet("SPAWN.world")){
            e.setRespawnLocation(plugin.getSpawnLocation());
        }
        else if(config.getBoolean("SETTINGS.spawnIfNoBed", true) && config.isSet("SPAWN.world")){
            if(player.getBedSpawnLocation() == null){
                e.setRespawnLocation(plugin.getSpawnLocation());
            }
        }
    }
}
