package se331.daybreaknews.repository;

import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByNewsIdAndUserId(Long newsId, Long userId);
    Long countByNewsIdAndVoteTypeAndCommentVisibleTrue(Long newsId, VoteType voteType);
}
