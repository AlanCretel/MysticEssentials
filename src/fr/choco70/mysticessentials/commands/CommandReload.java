package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.LocalesManager;
import fr.choco70.mysticessentials.utils.SQLiteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor{
    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final LocalesManager localesManager = plugin.getLocalesManager();
    private final SQLiteManager sqLiteManager = plugin.getSqLiteManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String playerLanguage = sqLiteManager.getPlayerLocale(player.getUniqueId());
            String configReloaded = localesManager.getMessage(playerLanguage, "CONFIG_RELOADED");
            player.sendMessage(configReloaded);
        }
        else{
            String serverLanguage = localesManager.getServerLocale();
            String configReloaded = localesManager.getMessage(serverLanguage, "CONFIG_RELOADED");
            sender.sendMessage(configReloaded);
        }
        return true;
    }
}
