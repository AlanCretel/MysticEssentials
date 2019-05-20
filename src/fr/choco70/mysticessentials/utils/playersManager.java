package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.entity.Player;

public class playersManager extends MysticEssentials{
    private MysticEssentials plugin = new MysticEssentials();


    public void createPlayerConfig(Player player){
        String fileName = player.getUniqueId().toString();
    }
}
