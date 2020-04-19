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
    public void setRating(Rating rating) throws RatingException {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> ratings = entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game", game).getResultList();

        int cnt=0;
        for (int i = 0; i < ratings.size(); i++) {
            cnt+=ratings.get(i).getRating();
        }return cnt/ratings.size();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        List<Rating> ratings = entityManager.createNamedQuery("Rating.getRating").setParameter("game", game).getResultList();
        for (int i = 0; i < ratings.size(); i++) {
            if (ratings.get(i).getPlayer().equals(player)) {
                return ratings.get(i).getRating();
            }

        }return 0;
    }
}
