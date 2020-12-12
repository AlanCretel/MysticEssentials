package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class PlayersManager {
    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    public void setLastLocation(Player player){
        if(sqLiteManager.haveLastLocation(player.getUniqueId())){
            sqLiteManager.updateLastLocation(player.getUniqueId(), player.getLocation());
        }
        else{
            sqLiteManager.setLastLocation(player.getUniqueId(), player.getLocation());
        }
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

    public List<String> toUserNames(List<UUID> userUUIDS){
        List<String> playerNames = new ArrayList<>();
        for (UUID userUUID : userUUIDS){
            playerNames.add(plugin.getServer().getOfflinePlayer(userUUID).getName());
        }
        return playerNames;
    }
}
