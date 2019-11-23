package de.fancy.minecasino;

import de.fancy.minecasino.commands.CMD_Spin;
import de.fancy.minecasino.listener.PlayerInventoryInteractListener;
import de.fancy.minecasino.utils.CasinoManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    public CasinoManager casinoManager = new CasinoManager();

    public String prefix = "§7[§4MineCasino§7] §7";

    @Override
    public void onEnable() {
        instance = this;

        System.out.println("[MineCasino] Das Plugin wurde erfolgreich gestartet!");

        this.getServer().getPluginManager().registerEvents(new PlayerInventoryInteractListener(), this);

        getCommand("spin").setExecutor(new CMD_Spin());
    }

    @Override
    public void onDisable() {
        System.out.println("[MineCasino] Das Plugin wurde erfolgreich gestoppt!");
    }

    public static Main getInstance() {
        return instance;
    }

    public CasinoManager getCasinoManager() {
        return casinoManager;
    }

    public String getPrefix() {
        return prefix;
    }
}
