package SnakeAndLadders.gamestudio.service;

import SnakeAndLadders.gamestudio.entity.Comment;


import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
}