package SnakeAndLadders.gamestudio.service;

import SnakeAndLadders.gamestudio.entity.Rating;


public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
}
