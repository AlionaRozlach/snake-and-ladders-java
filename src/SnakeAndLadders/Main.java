package SnakeAndLadders;

import SnakeAndLadders.consoleui.ConsoleUI;

public class Main {

    private int numOfPlayers = 1;
    public static void main(String[] args) {

        Field field = new Field(6,6,5,0 );
        ConsoleUI ui = new ConsoleUI(field);
        ui.gaming();
    }
}
