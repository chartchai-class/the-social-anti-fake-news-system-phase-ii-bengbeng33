package se331.daybreaknews.dao;

import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;

import java.util.Optional;

public interface VoteDao {
    Optional<Vote> findByNewsIdAndUserId(Long newsId, Long userId);
    Long countByNewsIdAndVoteType(Long newsId, VoteType voteType);
    Vote save(Vote vote);
}