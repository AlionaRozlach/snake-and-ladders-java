package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
 @Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
       // entityManager.persist(rating);
        try{
            int ratingId = ((Rating)entityManager.createQuery("SELECT r FROM Rating r WHERE r.game=:snakeAndladders AND r.player=:player").setParameter("player",rating.getPlayer())
            .setParameter("snakeAndladders",rating.getGame()).getSingleResult()).getIdent();
            Rating exist = entityManager.getReference(Rating.class,ratingId);
            exist.setRating(rating.getRating());
        }
        catch (RuntimeException e)
        {
           System.out.println("sorry");
        }
    }

    @Override
    public int getAverageRating(String game) {
        List<Rating> ratings = entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game", game).getResultList();

        int cnt=0;
        for (int i = 0; i < ratings.size(); i++) {
            cnt+=ratings.get(i).getRating();
        }return cnt/ratings.size();
    }

    @Override
    public int getRating(String game, String player){
        List<Rating> ratings = entityManager.createNamedQuery("Rating.getRating").setParameter("game", game).getResultList();
        for (int i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).getPlayer().equals(player)) {
                return ratings.get(i).getRating();
            }

        }return 0;
    }
}
