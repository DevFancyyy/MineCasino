package de.fancy.minecasino.listener;

import de.fancy.minecasino.Main;
import de.fancy.minecasino.utils.Spin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInventoryInteractListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if(clickedInventory != null) {
            if (Main.getInstance().getCasinoManager().hasPlayerActiveSpin(player)) {
                Spin playerSpin = Main.getInstance().getCasinoManager().getPlayerSpin(player);

                if (clickedInventory.equals(playerSpin.spinInventory)) {
                    event.setCancelled(true);

                    if(!playerSpin.isSpinning) {
                        ItemStack stakeItem = clickedInventory.getItem(10);

                        if (clickedItem.getData().getItemType() == Material.STAINED_CLAY) {
                            if (clickedItem.getData().getData() == 5) {
                                if (stakeItem.getAmount() + 1 < 65 && stakeItem.getAmount() + 1 <= player.getLevel()) {
                                    stakeItem.setAmount(stakeItem.getAmount() + 1);
                                    ItemMeta newStakeItemMeta = stakeItem.getItemMeta();
                                    newStakeItemMeta.setDisplayName("§e" + stakeItem.getAmount() + " Level");
                                    stakeItem.setItemMeta(newStakeItemMeta);
                                } else {
                                    player.sendMessage(Main.getInstance().getPrefix() + "§cDu kannst nicht mehr setzen!");
                                }
                            } else if (clickedItem.getData().getData() == 14) {
                                if (stakeItem.getAmount() - 1 > 0) {
                                    stakeItem.setAmount(stakeItem.getAmount() - 1);
                                    ItemMeta newStakeItemMeta = stakeItem.getItemMeta();
                                    newStakeItemMeta.setDisplayName("§e" + stakeItem.getAmount() + " Level");
                                    stakeItem.setItemMeta(newStakeItemMeta);
                                } else {
                                    player.sendMessage(Main.getInstance().getPrefix() + "§cDu kannst nicht weniger setzen!");
                                }
                            }
                        } else if (clickedItem.getData().getItemType() == Material.TRIPWIRE_HOOK) {
                            player.setLevel(player.getLevel() - stakeItem.getAmount());

                            playerSpin.spin(stakeItem.getAmount());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();

        if(closedInventory != null && Main.getInstance().getCasinoManager().hasPlayerActiveSpin(player)) {
            if(closedInventory.equals(Main.getInstance().getCasinoManager().getPlayerSpin(player).spinInventory)) {
                if(!Main.getInstance().getCasinoManager().getPlayerSpin(player).isFinished) {
                    Main.getInstance().getCasinoManager().getPlayerSpin(player).payoutWin();
                }

                Main.getInstance().getCasinoManager().removePlayerSpin(player);
            }
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        Player player = (Player) event.getSource().getHolder();
        Inventory targetInventory = event.getDestination();

        if(targetInventory != null && Main.getInstance().getCasinoManager().hasPlayerActiveSpin(player)) {
            if (targetInventory.equals(Main.getInstance().getCasinoManager().getPlayerSpin(player).spinInventory)) {
                event.setCancelled(true);
            }
        }
    }

}
