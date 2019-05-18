package fr.choco70.mysticessentials.commands;

import fr.choco70.mysticessentials.MysticEssentials;
import fr.choco70.mysticessentials.utils.langsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandTpDeny implements CommandExecutor{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private FileConfiguration config = plugin.getConfig();
    private langsManager langsManager = new langsManager();
    private String serverLanguage = config.getString("SETTINGS.serverLanguage", "en_us");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(sender instanceof Player){
            Player player = (Player)sender;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(player.getUniqueId().toString()));
            String playerLanguage = playerConfig.getString("language", serverLanguage);
            String teleportationDeniedSender = langsManager.getMessage(playerLanguage, "TELEPORTATION_DENIED_SENDER", "Teleportation request denied.");
            if(plugin.getTpa().containsKey(player)){
                Player requester = plugin.getTpa().get(player);
                FileConfiguration requesterConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(requester.getUniqueId().toString()));
                String requesterLanguage = requesterConfig.getString("language", serverLanguage);
                String teleportationDeniedRequester = langsManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER", "#target# denied your teleportation request.");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpa().remove(player);
            }
            else if(plugin.getTpahere().containsKey(player)){
                Player requester = plugin.getTpahere().get(player);
                FileConfiguration requesterConfig = YamlConfiguration.loadConfiguration(plugin.getPlayerFile(requester.getUniqueId().toString()));
                String requesterLanguage = requesterConfig.getString("language", serverLanguage);
                String teleportationDeniedRequester = langsManager.getMessage(requesterLanguage, "TELEPORTATION_DENIED_REQUESTER", "#target# denied your teleportation request.");
                player.sendMessage(teleportationDeniedSender);
                requester.sendMessage(formatString(teleportationDeniedRequester, player));
                plugin.getTpahere().remove(player);
            }
            else{
                String noTeleportationRequest = langsManager.getMessage(playerLanguage, "NO_TELEPORTATION_REQUEST", "You don't have any teleportation request.");
                player.sendMessage(noTeleportationRequest);
            }
        }
        else{
            String onlyPlayersWarn = langsManager.getMessage(serverLanguage, "ONLY_PLAYERS_COMMAND", "Only players can use this command.");
            sender.sendMessage(onlyPlayersWarn);
        }
        return true;
    }

    public String formatString(String string, Player target){
        String target_placeholder = "#target#";
        String formatedString = string.replaceAll(target_placeholder, target.getName());
        return formatedString;
    }
}
