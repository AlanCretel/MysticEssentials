package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyLink{

    private static Economy economy = null;
    private final MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private final FileConfiguration config = plugin.getConfig();
    private final PlayersManager playersManager = plugin.getPlayersManager();
    private final LocalesManager localesManager = plugin.getLocalesManager();

    public EconomyLink(){
        setupEconomy();
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public boolean isActiveEconomy(){
        return economy != null;
    }

    public double getPlayerBalance(Player player){
        return economy.getBalance(player);
    }

    public void addMoney(Player player, double amount){
        economy.depositPlayer(player, amount);
    }

    public void removeMoney(Player player, double amount){
        economy.withdrawPlayer(player, amount);
    }

    public boolean transferMoney(Player source, Player target, double amount){
        if(getPlayerBalance(source) >= amount){
            addMoney(target, amount);
            removeMoney(source, amount);
            return true;
        }
        else{
            return false;
        }
    }

    public void resetMoney(Player player){
        removeMoney(player, getPlayerBalance(player));
    }

    public void setMoney(Player player, double amount){
        double playerBalance = getPlayerBalance(player);
        if(playerBalance > amount){
            removeMoney(player, playerBalance - amount);
        }
        else if(playerBalance < amount){
            addMoney(player, amount - playerBalance);
        }
    }

}
