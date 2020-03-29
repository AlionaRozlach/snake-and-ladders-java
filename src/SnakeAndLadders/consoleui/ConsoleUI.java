package SnakeAndLadders.consoleui;

import SnakeAndLadders.Field;
import SnakeAndLadders.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private Player player;
    private Field field;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public ConsoleUI(Field field) {
        this.field = field;
    }
    private static final Pattern INPUT_PATTERN = Pattern.compile("[RX]");
    private int roll;


    private void handleInput() {
        while (true) {
            System.out.println("Please enter your selection <X> EXIT, <R> THROW DICE: ");
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();

            if ("X".equals(input))
                System.exit(0);

            Matcher matcher = INPUT_PATTERN.matcher(input);
            if (matcher.matches()) {
                try {
                    if ("R".equals(matcher.group(0))) {
                        roll = player.throwDice();
                       // field.move(player, roll);
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("You entered an invalid character!");
                    return;
                }
            }
        }
    }
//

    private void printField()
    {
        printFieldHeader();
        printFieldBody();
        //show();
    }



    private void printFieldHeader() {
       //System.out.printf("%1s","");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.printf("%4s", "___");
        }
        System.out.println();

    }

    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print(("|"));
            for (int column = 0; column < field.getColumnCount(); column++) {
                System.out.print(" ");
                field.printTile(row,column);
            }
            System.out.print(("|"));
            System.out.println();
        }
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.printf("%4s", "———");
        }
        System.out.println();
    }

    private void textOfGame(int num)
    {
        if(num==3) {
            System.out.println("At the beginning of the game you are on top of the field in the left corner at position number 1.");
        }
        else if(num == 4)
        {
            System.out.println(" Rozlach Alona.\n Student of the Technical University in Kosice, Slovakia.\n Specialty in computer science.\n Subject: component programming.\n Teacher: Ing. Jana Šťastná. " +
                    "Lecturer:Jaroslav Porubän.");
        }
    }

    private void menu()
    {
        Scanner in =  new Scanner(System.in);
        System.out.println("Select item number: ");
        System.out.println("1.One player" + '\n' + "2.Two player" + '\n' + "3.Rules of the game" + '\n' + "4.About author");

        int num = in.nextInt();

        if(num == 3)
        {
           textOfGame(3);
        }
        else if(num == 4) textOfGame(4);
    }

    boolean solved = false;

        public void gaming ()
        {

            player = new Player(1);
            System.out.println("--------------------------------SNAKE AND LADDERS----------------------------------");
            menu();
           do{
              // show();
               printField();
               handleInput();
               solved = field.move(player,roll);
           }while(solved == false);
           printField();
            //show();

            /* while (solved == false) {



                show();
                handleInput();
                //field.printTile();
                //field.move(player,roll);


               // System.out.println("----------------------------------------------------------------");

            }*/
        }

 /*   private void printField() {
        printFieldHeader();
        printFieldBody();
    }

    private void printFieldBody() {

    }

    private void printTile(int row, int column) {
        final Tile tile = field.getTile(row, column);
        switch (tile.getState()) {
            case FREE:
                if (tile instanceof Clue)
                    System.out.print(((Clue) tile).getValue());
                else
                    System.out.print("X");
                break;
            case SNAKE:
                System.out.print("X");
                break;
            case LADDER:
                System.out.print("H");
                break;
        }
    }
*/

    }
