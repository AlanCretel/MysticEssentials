package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class delaysManager{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);

    public int getTpaDelay(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> tpaDelays = new ArrayList<>();
        tpaDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.tpa", 2));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.delay.tpa.")){
                String delay = permission.getPermission().replace("mysticessentials.delay.tpa.", "");
                if(delay.equalsIgnoreCase("bypass")){
                    return 0;
                }
                else{
                    tpaDelays.add(Integer.parseInt(delay));
                }
            }
        }
        return Collections.min(tpaDelays);
    }

    public int getTpaHereDelay(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> tpaHereDelays = new ArrayList<>();
        tpaHereDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.tpahere", 2));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.delay.tpahere.")){
                String delay = permission.getPermission().replace("mysticessentials.delay.tpahere.", "");
                if(delay.equalsIgnoreCase("bypass")){
                    return 0;
                }
                else{
                    tpaHereDelays.add(Integer.parseInt(delay));
                }
            }
        }
        return Collections.min(tpaHereDelays);
    }

    public int getHomeDelay(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> homeDelays = new ArrayList<>();
        homeDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.home", 2));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.delay.home.")){
                String delay = permission.getPermission().replace("mysticessentials.delay.home.", "");
                if(delay.equalsIgnoreCase("bypass")){
                    return 0;
                }
                else{
                    homeDelays.add(Integer.parseInt(delay));
                }
            }
        }
        return Collections.min(homeDelays);
    }

    public int getBackDelay(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> backDelays = new ArrayList<>();
        backDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.back", 2));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.delay.back.")){
                String delay = permission.getPermission().replace("mysticessentials.delay.back.", "");
                if(delay.equalsIgnoreCase("bypass")){
                    return 0;
                }
                else{
                    backDelays.add(Integer.parseInt(delay));
                }
            }
        }
        return Collections.min(backDelays);
    }

    public int getSpawnDelay(Player player){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> spawnDelays = new ArrayList<>();
        spawnDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.spawn", 0));
        for (PermissionAttachmentInfo permission : permissions) {
            if(permission.getPermission().contains("mysticessentials.delay.spawn.")){
                String delay = permission.getPermission().replace("mysticessentials.delay.spawn.", "");
                if(delay.equalsIgnoreCase("bypass")){
                    return 0;
                }
                else{
                    spawnDelays.add(Integer.parseInt(delay));
                }
            }
        }
        return Collections.min(spawnDelays);
    }

    public int getWarpDelay(Player player, String warpName){
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        ArrayList<Integer> warpDelays = new ArrayList<>();
        warpDelays.add(plugin.getConfig().getInt("SETTINGS.default_delays.warp", 2));
        if(plugin.getConfig().getBoolean("SETTINGS.per_warp_delay", false)) {
            for (PermissionAttachmentInfo permission : permissions) {
                if (permission.getPermission().contains("mysticessentials.delay.warp.")) {
                    String delay = permission.getPermission().replace("mysticessentials.delay.warp.", "");
                    if (delay.equalsIgnoreCase("bypass")) {
                        return 0;
                    }
                    else {
                        warpDelays.add(Integer.parseInt(delay));
                    }
                }
            }
        }
        else {
            for (PermissionAttachmentInfo permission : permissions) {
                if (permission.getPermission().contains("mysticessentials.delay.warp." + warpName + ".")) {
                    String delay = permission.getPermission().replace("mysticessentials.delay.warp." + warpName + ".", "");
                    if (delay.equalsIgnoreCase("bypass")) {
                        return 0;
                    }
                    else {
                        warpDelays.add(Integer.parseInt(delay));
                    }
                }
            }
        }
        return Collections.min(warpDelays);
    }

}
