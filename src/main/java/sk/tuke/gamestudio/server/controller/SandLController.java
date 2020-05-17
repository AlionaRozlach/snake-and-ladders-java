


package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import sk.tuke.gamestudio.game.snakeAndLad.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.snakeAndLad.core.*;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/snakeAndladders")
public class SandLController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserController userController;

    private List<Player> players = new ArrayList<Player>();
    private Field field;

    private int count =0;
    private Dice dice = new Dice();
   public int k;
   public String l;
    public String tr;
public String loggedUser;
private Player player;
public String pos;
public boolean canMove;

    @RequestMapping
    public String snakeAndladders(@RequestParam(value="text",required =false) String text,
                                  @RequestParam(value="rating",required =false) String rating, Model model) {


        if (field == null) {
            newGame();
        }


        if(isLogged() && text!=null)
        {
            commentService.addComment(new Comment(loggedUser,"snakeAndladders",text,new Date()));
        }

        if(isLogged() && rating!=null)
        {
            ratingService.setRating(new Rating(loggedUser,"snakeAndladders",Integer.parseInt(rating),new Date()));
        }
        prepareModel(model);
        return "snakeAndladders";
    }


    public String namePlayer;



    @RequestMapping("/dice")
    public String dice(Model model)
    {
       k=dice.generateRandomDice();
       l=String.valueOf(k);

      pos =field.move(player,k);
        try{
            if(isLogged() && player.getPosition() ==100) {
                scoreService.addScore(new Score("snakeAndladders", loggedUser, field.getScore(), new Date()));
            }

        }catch (NumberFormatException e) {
            //Jaro: Zle poslane nic sa nedeje
            e.printStackTrace();
        }
        prepareModel(model);

        model.addAttribute("pos",pos);
       return "snakeAndladders";
    }

    public boolean isClick() {
        return l != null;
    }

    //Tento pristup sice nie je idealny, ale pre zaciatok je najjednoduchsi
    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.append("<td>\n");

                Tile tile = field.getTile(row,column);

                count++;
                if(player==null || (player.getPosition() != tile.getNum()))
                {
                    sb.append("<img src='/image/LL" + count + ".png'>");
                }
                if(player!=null) {
                    if (player.getPosition() == tile.getNum()) {
                        sb.append("<img src='/image/fishka/L" +count+ ".png'>");
                    }
                }
            }

            if (field.equals(this.field))
                sb.append("</a>\n");
            sb.append("</td>\n");
        }

        count=0;
            sb.append("</tr>\n");

        sb.append("</table>\n");

        return sb.toString();
    }



    private void prepareModel(Model model) {
        model.addAttribute("scores", scoreService.getBestScores("snakeAndladders"));
        model.addAttribute("comments",commentService.getComments("snakeAndladders"));
        model.addAttribute("avrating",ratingService.getAverageRating("snakeAndladders"));
        if ((ratingService.getRating("snakeAndladders",loggedUser))!=0){
            model.addAttribute("ratingPlayer",ratingService.getRating("snakeAndladders",loggedUser));
        }
    }

    private void newGame() {
        field = new Field(10, 10);
    }

    @RequestMapping("/login")
    public String login(String login, Model model) {
        loggedUser = login;
        player = new Player(login);
//        model.addAttribute("comments",commentService.getComments("snakeAndladders"));
//
//        model.addAttribute("scores", scoreService.getBestScores("snakeAndladders"));
//        model.addAttribute("rating",ratingService.getAverageRating("snakeAndladders"));
        prepareModel(model);
        return "snakeAndladders";
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        loggedUser = null;
        return "snakeAndladders";
    }


    public String getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }


}
