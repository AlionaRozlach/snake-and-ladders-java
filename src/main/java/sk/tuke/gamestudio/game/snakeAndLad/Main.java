package sk.tuke.gamestudio.game.snakeAndLad;


import sk.tuke.gamestudio.game.snakeAndLad.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.RatingException;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException, CommentException, RatingException {

        ConsoleUI ui = new ConsoleUI();
        ui.gaming();
    }

}
