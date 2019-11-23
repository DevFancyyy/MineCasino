package de.fancy.minecasino.utils;

import de.fancy.minecasino.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CasinoManager {
    public HashMap<Player, Spin> playerSpins = new HashMap<>();

    public CasinoManager() {

    }

    public boolean hasPlayerActiveSpin(Player player) {
        return playerSpins.containsKey(player);
    }

    public Spin getPlayerSpin(Player player) {
        if(hasPlayerActiveSpin(player)) {
            return playerSpins.get(player);
        } else {
            return null;
        }
    }

    public void setPlayerSpin(Player player, Spin spin) {
        playerSpins.put(player, spin);
    }

    public void removePlayerSpin(Player player) {
        playerSpins.remove(player);
    }

    public void throwDice(Player player, int dices) {
        Dice thrownDice = new Dice(dices);
        thrownDice.roll();
        int dicedNumber = thrownDice.getDicedNumber();

        String dice = "Würfel";
        if(dices > 1) {
            dice = "Würfeln";
        }

        player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast mit §a" + dices + " §7" + dice + " eine §a" + dicedNumber + " §7gewürfelt!");

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(player.getLocation().distance(onlinePlayer.getLocation()) <= 7 && onlinePlayer != player) {
                onlinePlayer.sendMessage(Main.getInstance().getPrefix() + player.getDisplayName() + " §7hat mit §a" + dices + " §7" + dice + " eine §a" + dicedNumber + " §7gewürfelt!");
            }
        }
    }

}
