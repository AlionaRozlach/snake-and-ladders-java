package SnakeAndLadders;

import java.util.Random;

public class Dice {
    private Random rand;

    public Dice() {
        rand = new Random();//
    }


    public int generateRandomDice() {
        int num = 1+ rand.nextInt(6);
        System.out.println("Your number: " + num);
        return num;
    }//
}
