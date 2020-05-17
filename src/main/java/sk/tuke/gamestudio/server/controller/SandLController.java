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
import sk.tuke.gamestudio.game.snakeAndLad.core.Dice;
import sk.tuke.gamestudio.game.snakeAndLad.core.Field;
import sk.tuke.gamestudio.game.snakeAndLad.core.Player;
import sk.tuke.gamestudio.game.snakeAndLad.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import java.util.Date;


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
    private Field field;
    private int count = 0;
    private Dice dice = new Dice();
    private Player player;
    public int k;
    public String l;
    public String loggedUser;
    public String pos;

    @RequestMapping
    public String snakeAndladders(@RequestParam(value = "text", required = false) String text,
                                  @RequestParam(value = "rating", required = false) String rating, Model model) {


        if (field == null) {
            newGame();
        }


        if (isLogged() && text != null) {
            commentService.addComment(new Comment(loggedUser, "snakeAndladders", text, new Date()));
        }

        if (isLogged() && rating != null) {
            ratingService.setRating(new Rating(loggedUser, "snakeAndladders", Integer.parseInt(rating), new Date()));
        }
        prepareModel(model);
        return "snakeAndladders";
    }


    @RequestMapping("/new")
    public String newGame(Model model) {
        l = "";
        player.setPosition(1);
        newGame();
        return "snakeAndladders";
    }


    @RequestMapping("/dice")
    public String dice(Model model) {
        k = dice.generateRandomDice();
        l = String.valueOf(k);

        pos = field.move(player, k);
        try {
            if (isLogged() && player.getPosition() == 100) {
                scoreService.addScore(new Score("snakeAndladders", loggedUser, field.getScore(), new Date()));
            }

        } catch (NumberFormatException e) {
            //Jaro: Zle poslane nic sa nedeje
            e.printStackTrace();
        }

        if (isLogged() && player.getPosition() == 100) {
            model.addAttribute("done", true);
        }

        prepareModel(model);

        model.addAttribute("pos", pos);
        return "snakeAndladders";
    }

    public boolean isClick() {
        if (isLogged() && player.getPosition() == 100) {
            return false;
        }

        return true;
    }

    //Tento pristup sice nie je idealny, ale pre zaciatok je najjednoduchsi
    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.append("<td>\n");

                Tile tile = field.getTile(row, column);

                count++;
                if (player == null || (player.getPosition() != tile.getNum())) {
                    sb.append("<img src='/image/LL" + count + ".png'>");
                }
                if (player != null) {
                    if (player.getPosition() == tile.getNum()) {
                        sb.append("<img src='/image/fishka/L" + count + ".png'>");
                    }
                }
            }

            if (field.equals(this.field))
                sb.append("</a>\n");
            sb.append("</td>\n");
        }

        count = 0;
        sb.append("</tr>\n");

        sb.append("</table>\n");

        return sb.toString();
    }

    @RequestMapping("/login")
    public String login(String login, Model model) {
        loggedUser = login;
        player = new Player(login);
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


    private void prepareModel(Model model) {
        model.addAttribute("scores", scoreService.getBestScores("snakeAndladders"));
        model.addAttribute("comments", commentService.getComments("snakeAndladders"));
        model.addAttribute("avrating", ratingService.getAverageRating("snakeAndladders"));
        if ((ratingService.getRating("snakeAndladders", loggedUser)) != 0) {
            model.addAttribute("ratingPlayer", ratingService.getRating("snakeAndladders", loggedUser));
        }
    }

    private void newGame() {
        field = new Field(10, 10);
    }

}
