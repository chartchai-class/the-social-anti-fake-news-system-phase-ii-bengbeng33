package se331.daybreaknews.repository;

import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByNewsId(Long newsId);
    Optional<Vote> findByNewsIdAndUserIdentifier(Long newsId, String userIdentifier);
    Long countByNewsIdAndVoteType(Long newsId, VoteType voteType);
}