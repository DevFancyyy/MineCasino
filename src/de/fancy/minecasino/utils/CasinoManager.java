package de.fancy.minecasino.utils;

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

}
