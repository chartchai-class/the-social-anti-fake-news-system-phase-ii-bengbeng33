package se331.daybreaknews.dao;

import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;

import java.util.List;
import java.util.Optional;

public interface VoteDao {
    Optional<Vote> findByNewsIdAndUserIdentifier(Long newsId, String userIdentifier);
    Long countByNewsIdAndVoteType(Long newsId, VoteType voteType);
    Vote save(Vote vote);
    List<Vote> findByNewsId(Long newsId);
}