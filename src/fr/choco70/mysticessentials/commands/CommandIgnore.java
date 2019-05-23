package fr.choco70.mysticessentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandIgnore implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        sender.sendMessage("Command in construction");
        return true;
    }
}
