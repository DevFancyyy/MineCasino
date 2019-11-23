package de.fancy.minecasino.utils;

import de.fancy.minecasino.Main;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Spin {

    public static List<ItemStack> spinItems = new ArrayList();

    public Player player;

    public int spinRunnable;
    public Inventory spinInventory = Bukkit.createInventory(null, 45, "§4Slot Machine");
    public List<List<ItemStack>> spinRows = new ArrayList<>();
    public int spins;
    public int rowChange = 0;

    public int stake;
    public int win;
    public HashMap<Material, Integer> winningLine = new HashMap<>();
    public boolean isSpinning = false;
    public boolean isFinished = false;

    public Spin(Player player) {
        this.player = player;
        addItemsToSpinItems();
        prepareSpin();
    }

    public void addItemsToSpinItems() {
        spinItems.add(new ItemStack(Material.EMERALD)); //x50
        spinItems.add(new ItemStack(Material.DIAMOND)); //x25
        spinItems.add(new ItemStack(Material.GOLD_INGOT)); //x10
        spinItems.add(new ItemStack(Material.IRON_INGOT)); //x5
        spinItems.add(new ItemStack(Material.GLOWSTONE_DUST)); //LOOSE
        spinItems.add(new ItemStack(Material.GOLD_NUGGET)); //x2
        spinItems.add(new ItemStack(Material.COAL)); //LOOSE
        spinItems.add(new ItemStack(Material.NETHER_STAR)); //x2
        spinItems.add(new ItemStack(Material.PRISMARINE_CRYSTALS)); //LOOSE
        spinItems.add(new ItemStack(Material.SUGAR)); //LOOSE
        spinItems.add(new ItemStack(Material.RED_MUSHROOM)); //LOOSE
        spinItems.add(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 2)); //LOOSE
    }

    public void prepareSpin() {
        spinRows.clear();

        for(int i = 0; i < 9; i++) {
            List<ItemStack> lotteryRow = new ArrayList<>();

            for(int j = 0; j < 5; j++) {
                Random random = new Random();
                int itemNumber = random.nextInt(spinItems.size());

                lotteryRow.add(new ItemStack(spinItems.get(itemNumber)));

                if(i < 5) {
                    spinInventory.setItem((i * 9) + 3 + j, new ItemStack(spinItems.get(itemNumber)));
                }
            }

            spinRows.add(lotteryRow);
        }
    }

    public void openSpinInventory() {
        player.openInventory(spinInventory);

        for(int i = 0; i < 45; i = i + 9) {
            ItemStack whitePane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
            ItemStack redPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);

            if(i != 18) {
                spinInventory.setItem(i, whitePane);
                spinInventory.setItem(i+1, whitePane);
                spinInventory.setItem(i+2, whitePane);
                spinInventory.setItem(i+8, whitePane);
            } else {
                spinInventory.setItem(i, redPane);
                spinInventory.setItem(i+1, redPane);
                spinInventory.setItem(i+2, redPane);
                spinInventory.setItem(i+8, redPane);
            }
        }

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                spinInventory.setItem((i * 9) + 3 + j, new ItemStack(spinRows.get(i).get(j)));
            }
        }

        ItemStack higherItem = new ItemStack(Material.STAINED_CLAY, 1, (byte) 5);
        ItemMeta higherItemMeta = higherItem.getItemMeta();
        higherItemMeta.setDisplayName("§aEinsatz erhöhen");
        higherItem.setItemMeta(higherItemMeta);

        ItemStack lowerItem = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
        ItemMeta lowerItemMeta = lowerItem.getItemMeta();
        lowerItemMeta.setDisplayName("§cEinsatz verringern");
        lowerItem.setItemMeta(lowerItemMeta);

        ItemStack stakeItem = new ItemStack(Material.EMERALD);
        ItemMeta stakeItemMeta = stakeItem.getItemMeta();
        stakeItemMeta.setDisplayName("§b1 Level");
        stakeItem.setItemMeta(stakeItemMeta);

        ItemStack playItem = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta playItemMeta = playItem.getItemMeta();
        playItemMeta.setDisplayName("§aSpielen");
        playItem.setItemMeta(playItemMeta);

        spinInventory.setItem(1, higherItem);
        spinInventory.setItem(10, stakeItem);
        spinInventory.setItem(19, lowerItem);
        spinInventory.setItem(37, playItem);
    }

    public void changeRows(int changingRows) {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5-changingRows; j++) {
                int newRow = i + rowChange;

                if(newRow >= spinRows.size()) {
                    newRow = i + rowChange - spinRows.size();
                }

                spinInventory.setItem((i * 9) + (3 + changingRows + j), new ItemStack(spinRows.get(newRow).get(j)));
            }
        }

        if(rowChange == 8) {
            rowChange = 0;
        } else {
            rowChange++;
        }
    }

    public void spin(int stake) {
        this.stake = stake;
        this.isSpinning = true;
        this.isFinished = false;
        this.rowChange = 0;
        this.spins = 0;

        this.winningLine.clear();

        prepareSpin();

        spinRunnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                spins++;

                if(spins <= 25 && spins > -1) {
                    changeRows(0);
                } else if(spins <= 50 && spins > 25) {
                    changeRows(1);
                } else if(spins <= 75 && spins > 50) {
                    changeRows(2);
                } else if(spins <= 100 && spins > 75) {
                    changeRows(3);
                } else if(spins < 125 && spins > 100) {
                    changeRows(4);
                } else if(spins == 125) {
                    changeRows(4);

                    isSpinning = false;
                    isFinished = true;
                    payoutWin();
                }

                player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.C));
            }
        }, 5, 1);
    }

    public void payoutWin() {
        Bukkit.getScheduler().cancelTask(spinRunnable);

        if(isFinished == true) {
            for(int i = 20; i < 26; i++) {
                if(!winningLine.containsKey(spinInventory.getItem(i).getType())) {
                    winningLine.put(spinInventory.getItem(i).getType(), 1);
                } else {
                    winningLine.replace(spinInventory.getItem(i).getType(), winningLine.get(spinInventory.getItem(i).getType())+1);
                }
            }
        } else {
            Random randomItem = new Random();
        }

        this.win = getMultiplier();

        if(this.win >= this.stake) {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §a" + win + " §7Erfahrungslevel gewonnen!");
            player.setLevel(player.getLevel() + win);
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "§7Du hast §4" + stake + " §7Erfahrungslevel verloren!");
        }
    }

    public int getMultiplier() {
        int multiplier = 0;

        if(winningLine.containsKey(Material.EMERALD) && winningLine.get(Material.EMERALD) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.EMERALD) * 25);
        }

        if(winningLine.containsKey(Material.DIAMOND) && winningLine.get(Material.DIAMOND) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.DIAMOND) * 15);
        }

        if(winningLine.containsKey(Material.GOLD_INGOT) && winningLine.get(Material.GOLD_INGOT) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.GOLD_INGOT) * 10);
        }

        if(winningLine.containsKey(Material.IRON_INGOT) && winningLine.get(Material.IRON_INGOT) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.IRON_INGOT) * 5);
        }

        if(winningLine.containsKey(Material.NETHER_STAR) && winningLine.get(Material.NETHER_STAR) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.NETHER_STAR) * 2);
        }

        if(winningLine.containsKey(Material.GOLD_NUGGET) && winningLine.get(Material.GOLD_NUGGET) >= 2) {
            multiplier = multiplier + (winningLine.get(Material.GOLD_NUGGET) * 2);
        }

        return multiplier * stake;
    }
}
