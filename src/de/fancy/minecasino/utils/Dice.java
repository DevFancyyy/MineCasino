package de.fancy.minecasino.utils;

import java.util.Random;

public class Dice {
    public int amount;
    public final int eyes = 6;
    public int dicedNumber;

    public Dice(int dices) {
        this.amount = dices;
    }

    public void roll() {
        Random random = new Random();

        dicedNumber = random.nextInt(eyes * amount);

        if(dicedNumber < amount) {
            dicedNumber = amount;
        }
    }

    public int getDicedNumber() {
        return this.dicedNumber;
    }
}
