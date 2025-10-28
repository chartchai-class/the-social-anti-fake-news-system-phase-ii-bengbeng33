package se331.daybreaknews.dao;

import se331.daybreaknews.entity.Comment;

import java.util.List;

public interface CommentDao {
    List<Comment> findByNewsId(Long newsId);
    Comment save(Comment comment);
    void deleteComment(Long id);
}

