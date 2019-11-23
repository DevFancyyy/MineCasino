package de.fancy.minecasino.commands;

import de.fancy.minecasino.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Dice implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        Player player = (Player) sender;

        if(args.length == 0) {
            Main.getInstance().getCasinoManager().throwDice(player, 1);
        } else if(args.length == 1) {
            try {
                Main.getInstance().getCasinoManager().throwDice(player, Integer.parseInt(args[0]));
            } catch(NumberFormatException e) {
                player.sendMessage(Main.getInstance().getPrefix() + "§cDu musst eine Zahl angeben!");
            }
        } else if(args.length < 2) {
            player.sendMessage(Main.getInstance().getPrefix() + "§cDu hast zu viele Argumente angegeben!");
        }

        return true;
    }

}
