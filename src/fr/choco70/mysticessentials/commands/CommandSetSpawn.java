package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandSetSpawn implements CommandExecutor {

    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
        String serverLanguage = localesManager.getServerLocale();
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            config.set("SPAWN.world", player.getWorld().getName());
            config.set("SPAWN.x", player.getLocation().getX());
            config.set("SPAWN.y", player.getLocation().getY());
            config.set("SPAWN.z", player.getLocation().getZ());
            config.set("SPAWN.pitch", player.getLocation().getPitch());
            config.set("SPAWN.yaw", player.getLocation().getYaw());
            try {
                config.save(plugin.getDataFolder().toString() + "/config.yml");
                String spawnSet = localesManager.getMessage(playerLanguage, "SPAWN_SET");
                player.sendMessage(spawnSet);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            String onlyPlayersWarn = localesManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }
}
