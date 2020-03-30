package SnakeAndLadders.consoleui;

import SnakeAndLadders.Field;
import SnakeAndLadders.Player;
import SnakeAndLadders.gamestudio.entity.Comment;
import SnakeAndLadders.gamestudio.entity.Rating;
import SnakeAndLadders.gamestudio.entity.Score;
import SnakeAndLadders.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static SnakeAndLadders.Field.GAME_NAME;

public class ConsoleUI {



    private static final Pattern INPUT_PATTERN = Pattern.compile("[RX]");

    private int roll=0;
    private List<Player> listOfPlayer = new ArrayList<Player>();
    private Scanner in =  new Scanner(System.in);
    private int player_amount =0;
    private Field field;
    private int player_idx=0;
    private List<Score> scores;
    private Comment comment;
    private Rating rate;

    private CommentService commentService = new CommentServiceJDBC();
    private ScoreService scoreService = new ScoreServiceJDBC();
    private RatingService ratingService= new RatingServiceJDBC();




    public ConsoleUI() {
    }


    private void handleInput(Player player_now) {
        while (true) {
            System.out.println("Please enter your selection <X> EXIT, <R> THROW DICE: ");
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
                System.out.println("Bad input! Try again!");
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
                System.out.println("Please enter your name: ");
                String namePlayer = in.next();
                Player player = new Player(namePlayer);
                listOfPlayer.add(player);
            }

    }


    private void textOfGame(int num)
    {
        if(num==1)
        {
            System.out.println("Please enter the number of players(1-6): ");

            player_amount = in.nextInt();
            if(player_amount <=0|| player_amount >6) System.out.println("Invalid amount!");

            initPlayers();
            field = new Field(10,10 ,listOfPlayer);

        }
       /* else if(num==2) {
            System.out.println("At the beginning of the game you are on top of the field in the left corner at position number 1.");
        }
        else if(num == 3)
        {
            System.out.println(" Rozlach Alona.\n Student of the Technical University in Kosice, Slovakia.\n Specialty in computer science.\n Subject: component programming.\n Teacher: Ing. Jana Šťastná. " +
                    "Lecturer:Jaroslav Porubän.");
        }*/
    }

    private void menu()
    {
        System.out.println("Select item number: ");
        System.out.println("1.PLAY" + '\n' + "2.RULES OF THE GAME" + '\n' + "3.ABOUT AUTHOR");

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
            System.out.println("Best scores:" + scores.size());
            for (Score s : scores) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }

    }



    boolean solved = false;


    public void gaming () throws IOException, CommentException, RatingException {
        System.out.println("--------------------------------SNAKE AND LADDERS----------------------------------");
        menu();

        boolean done = false;
        while (!solved)
        {

            Player player_now = listOfPlayer.get(player_idx);
            printField();

           // handleInput(player_now);
            System.out.println(player_now.getName());
            int roll = player_now.throwDice();
            solved = field.move(player_now,roll);


            if(solved)
            {
                for(int i=0;i<player_amount;i++)
                {
                    Score score = new Score("S&L", listOfPlayer.get(i).getName(), listOfPlayer.get(i).getPosition(), new java.util.Date());
                    ScoreService scoreService = new ScoreServiceJDBC();
                    scoreService.addScore(score);
                    printScore();
                }

                for (int i=0;i<player_amount;i++)
                {
                    System.out.println("Do you want to leave a comment?[Y/N]: ");
                    String answer = in.next();
                    if(answer.equals("Y"))
                    {
                        BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
                        String commenting = is.readLine();
                        comment = new Comment(listOfPlayer.get(i).getName(),GAME_NAME,commenting, new java.util.Date());
                        commentService.addComment(comment);
                        System.out.println("Thank you!");
                    }
                    else if(answer.equals("N")){
                        System.out.println("Ok!Thank you!");
                    }
                    else{
                        System.out.println("Bad input!");
                        break;
                    }
                }
                for (int i=0;i<player_amount;i++)
                {
                    System.out.println("Do you want to rate the app?(1-5)[Y/N]: ");
                    String answer = in.next();
                    if(answer.equals("Y"))
                    {
                        System.out.println("Enter the number [1-5]: ");
                        int rating = in.nextInt();
                        if(rating>5|| rating<1)
                        {
                            System.out.println("Bad input!");
                            break;
                        }
                        else{

                            rate = new Rating(listOfPlayer.get(i).getName(),GAME_NAME,rating, new java.util.Date());

                            ratingService.setRating(rate);

                            System.out.println("Thank you!");

                            System.out.print("Average rating: " + ratingService.getAverageRating(GAME_NAME));
                        }

                    }
                    else if(answer.equals("N")){
                        System.out.println("Ok!Thank you!");
                    }
                    else{
                        System.out.println("Bad input!");
                        break;
                    }
                }
                return;
            }


            player_idx++;
            if(player_idx ==player_amount) player_idx =0;
        }
    }


}
