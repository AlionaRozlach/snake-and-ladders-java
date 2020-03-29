package SnakeAndLadders;

public class Player {
    private int position;
    private static Dice dice;
    public Player(int position) {
        dice = new  Dice();
        this.position = position;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /* public int setPosition(int position)
    {
        this.position = position;
    }*/


    public int throwDice(){

        return dice.generateRandomDice();
    }
}

