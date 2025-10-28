package se331.daybreaknews.service;

import se331.daybreaknews.dto.VoteDTO;

public interface VoteService {
    VoteDTO createVote(VoteDTO dto);
    boolean hasUserVoted(Long newsId, String userIdentifier);
    Long countVotesByNewsIdAndType(Long newsId, se331.daybreaknews.entity.VoteType voteType);
    long getFakeVotes(Long newsId);
    long getNotFakeVotes(Long newsId);
}
