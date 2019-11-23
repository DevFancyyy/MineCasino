package de.fancy.minecasino.commands;

import de.fancy.minecasino.Main;
import de.fancy.minecasino.utils.Spin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Spin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Spin playerSpin = new Spin(player);

        Main.getInstance().getCasinoManager().setPlayerSpin(player, playerSpin);

        playerSpin.openSpinInventory();

        return true;
    }
}
