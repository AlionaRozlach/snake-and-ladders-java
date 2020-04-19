package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;


public class RatingServiceJDBC implements RatingService {


    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "qtQm%H8N";
    public static final String SET_RATING =
            "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)";
    public static final String SELECT_RATING =
            "SELECT  player,game,rating, ratedon FROM rating WHERE game = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SET_RATING)) {
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setDate(4, new Date(rating.getRatedon().getTime()));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int sum = 0;
        int cnt = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        sum += rs.getInt(3);
                        cnt++;
                    }
                    sum /= cnt;
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading rating", e);
        }
        return sum;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getString(1).equals(player)) {
                            rating = rs.getInt(3);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error execute", e);
        }
        return rating;
    }

    public static void main(String[] args) throws Exception {
        Rating rating = new Rating("kola", "S&L", 5, new java.util.Date());
        RatingService ratingService = new RatingServiceJDBC();
        ratingService.setRating(rating);
        System.out.println(ratingService.getRating("S&L", "kola"));
    }
}
