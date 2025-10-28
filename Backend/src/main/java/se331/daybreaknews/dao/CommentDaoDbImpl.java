package se331.daybreaknews.dao;

import se331.daybreaknews.entity.Comment;
import se331.daybreaknews.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class CommentDaoDbImpl implements CommentDao {
    final CommentRepository commentRepository;

    @Override
    public List<Comment> findByNewsId(Long newsId) {
        return commentRepository.findByNewsIdOrderByCreatedAtDesc(newsId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

