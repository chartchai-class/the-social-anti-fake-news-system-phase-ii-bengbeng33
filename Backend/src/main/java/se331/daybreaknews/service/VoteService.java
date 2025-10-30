package se331.daybreaknews.service;

import se331.daybreaknews.entity.Comment;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;

public interface VoteService {
    Vote createVoteForComment(News news, User user, Comment comment, VoteType voteType);
    boolean hasUserVoted(Long newsId, Long userId);
    Long countVotesByNewsIdAndType(Long newsId, VoteType voteType);
    long getFakeVotes(Long newsId);
    long getNotFakeVotes(Long newsId);
}
