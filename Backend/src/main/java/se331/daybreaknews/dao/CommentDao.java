package se331.daybreaknews.dao;

import se331.daybreaknews.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    List<Comment> findByNewsId(Long newsId);
    List<Comment> findAllOrderByCreatedAtDesc();
    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    void deleteComment(Long id);
}
