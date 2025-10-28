package se331.daybreaknews.repository;

import se331.daybreaknews.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByNewsIdOrderByCreatedAtDesc(Long newsId);
}

