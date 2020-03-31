package SnakeAndLadders.core;

public class Player {
    private int position;
    private String name;
    private static Dice dice;


    public Player(String name) {
        dice = new  Dice();
        this.name = name;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }




    public int throwDice(){

        return dice.generateRandomDice();
    }
}

