package SnakeAndLadders.consoleui;

import SnakeAndLadders.core.Field;
import SnakeAndLadders.core.Player;
import SnakeAndLadders.gamestudio.entity.Comment;
import SnakeAndLadders.gamestudio.entity.Rating;
import SnakeAndLadders.gamestudio.entity.Score;
import SnakeAndLadders.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static SnakeAndLadders.core.Field.GAME_NAME;

public class ConsoleUI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private static final Pattern INPUT_PATTERN = Pattern.compile("[RX]");

    private int roll=0;
    private List<Player> listOfPlayer = new ArrayList<Player>();
    private Scanner in =  new Scanner(System.in);
    private int player_amount =0;
    private Field field;
    private int player_idx=0;
    private Comment comment;
    private Rating rate;
    private boolean done = false;

    private CommentService commentService = new CommentServiceJDBC();
    private ScoreService scoreService = new ScoreServiceJDBC();
    private RatingService ratingService= new RatingServiceJDBC();




    public ConsoleUI() {
    }


    private void handleInput(Player player_now) {
        while (true) {
            System.out.println(ANSI_YELLOW + "Please enter your selection <X> EXIT, <R> THROW DICE: " + ANSI_RESET);
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();

            if ("X".equals(input))
                System.exit(0);

            Matcher matcher = INPUT_PATTERN.matcher(input);
            if (matcher.matches()) {
                    if ("R".equals(matcher.group(0))) {
                        roll = player_now.throwDice();
                        return;
                    }

            } else
            {
                System.out.println(ANSI_RED + "Bad input! Try again!" + ANSI_RESET);
            }
        }
    }


    private void printField()
    {
        printFieldHeader();
        printFieldBody();
    }



    private void printFieldHeader() {
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


    private void initPlayers()
    {
            int i;

            for (i = 0; i < player_amount; i++) {
                System.out.println(ANSI_YELLOW+ "Please enter your name: " + ANSI_RESET);
                String namePlayer = in.next();
                Player player = new Player(namePlayer);
                listOfPlayer.add(player);
            }

    }


    private void textOfGame(int num)
    {
        if(num==1)
        {
            System.out.println(ANSI_RED+"Please enter the number of players(1-6): "+ANSI_RESET);

            player_amount = in.nextInt();
            if(player_amount <=0|| player_amount >6) System.out.println(ANSI_RED + "Invalid amount!" +ANSI_RED);

            initPlayers();
            field = new Field(6,6,listOfPlayer);

        }
        else if(num==2) {
            System.out.println(ANSI_YELLOW + "At the beginning of the game you are on top of the field in the left corner at position number 1."+
                    "By pressing [R], a number (the number of your steps) appears on the dice. You move further along the map.\n" +
                    "If you get from the stairs, you climb up (closer to the finish line). From the stairs - [^]. The top of the stairs - [H].\n" +
                    "If you fall on the head of the snake, then descend along it to the end of its tail. The head of the snake is [X], the tail is [S]. The one who first gets to the finish line wins (bottom left corner)." + ANSI_RESET);
            System.out.println();
            menu();
        }
        else if(num == 3)
        {
            System.out.println(ANSI_YELLOW +" Rozlach Alona.\n Student of the Technical University in Kosice, Slovakia.\n Specialty in computer science.\n Subject: component programming.\n Teacher: Ing. Jana Šťastná. " +
                    "Lecturer:Jaroslav Porubän." + ANSI_RESET);
            System.out.println();
            menu();
        }
        else
        {
            System.out.println(ANSI_RED + "Bad input!Try again!" +ANSI_RESET);
            System.out.println();
            menu();
        }
    }

    private void menu()
    {
        System.out.println(ANSI_BLUE + "Select item number: "+ANSI_RESET);
        System.out.println(ANSI_PURPLE + "1.PLAY" + '\n' + "2.RULES OF THE GAME" + '\n' + "3.ABOUT AUTHOR"+ ANSI_RESET);

        int num = in.nextInt();

        if(num==1) textOfGame(1);
        else if(num == 2)
        {
            textOfGame(2);
        }
        else if(num == 3) textOfGame(3);
    }


    private void printScore() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);
            System.out.println(ANSI_YELLOW + "Best scores:" + scores.size()+ANSI_RESET);
            for (Score s : scores) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }

    }

    private void score() {
        for (int i = 0; i < player_amount; i++) {
            Score score = new Score("S&L", listOfPlayer.get(i).getName(), listOfPlayer.get(i).getPosition(), new java.util.Date());
            ScoreService scoreService = new ScoreServiceJDBC();
            scoreService.addScore(score);
            printScore();
        }
    }

    private void comment() throws IOException, CommentException {
        for (int i=0;i<player_amount;i++) {
            System.out.println(ANSI_BLUE+"Do you want to leave a comment?[Y/N]: "+ ANSI_RESET);
            String answer = in.next();
            if (answer.equals("Y")) {
                BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
                String commenting = is.readLine();
                comment = new Comment(listOfPlayer.get(i).getName(), GAME_NAME, commenting, new java.util.Date());
                commentService.addComment(comment);
                System.out.println(ANSI_YELLOW+"Thank you!"+ANSI_RESET);
            } else if (answer.equals("N")) {
                System.out.println(ANSI_YELLOW+"Ok!Thank you!"+ANSI_RESET);
            } else {
                System.out.println(ANSI_RED+"Bad input!"+ANSI_RESET);
                break;
            }
        }
    }

    private void rating() throws RatingException {
        for (int i=0;i<player_amount;i++)
        {
            System.out.println(ANSI_BLUE+"Do you want to rate the app?(1-5)[Y/N]: "+ANSI_RESET);
            String answer = in.next();
            if(answer.equals("Y"))
            {
                System.out.println(ANSI_YELLOW+"Enter the number [1-5]: "+ANSI_RESET);
                int rating = in.nextInt();
                if(rating>5|| rating<1)
                {
                    System.out.println(ANSI_RED+"Bad input!"+ANSI_RESET);
                    break;
                }
                else{

                    rate = new Rating(listOfPlayer.get(i).getName(),GAME_NAME,rating, new java.util.Date());

                    ratingService.setRating(rate);

                    System.out.println(ANSI_YELLOW+"Thank you!"+ANSI_RESET);

                    System.out.print(ANSI_BLUE+"Average rating: " + ratingService.getAverageRating(GAME_NAME)+ANSI_RESET);
                }

            }
            else if(answer.equals("N")){
                System.out.println(ANSI_BLUE+"Ok!Thank you!"+ANSI_BLUE);
            }
            else{
                System.out.println(ANSI_RED+"Bad input!"+ANSI_RESET);
                break;
            }
        }
    }
    boolean solved = false;


    public void gaming () throws IOException, CommentException, RatingException {
        System.out.println(ANSI_RED + "--------------------------------SNAKE AND LADDERS----------------------------------"+ANSI_RESET);
        menu();

        while (!solved)
        {

            Player player_now = listOfPlayer.get(player_idx);
            printField();

            handleInput(player_now);
            System.out.println(player_now.getName());
            //int roll = player_now.throwDice();
            solved = field.move(player_now,roll);


            if(solved)
            {
                score();
                comment();
                rating();
                return;
            }


            player_idx++;
            if(player_idx ==player_amount) player_idx =0;
        }
    }


}
