package se331.daybreaknews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se331.daybreaknews.dao.NewsDao;
import se331.daybreaknews.dao.VoteDao;
import se331.daybreaknews.entity.Comment;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    final VoteDao voteDao;
    final NewsDao newsDao;

    @Override
    @Transactional
    public Vote createVoteForComment(News news, User user, Comment comment, VoteType voteType) {
        if (news == null) {
            throw new IllegalArgumentException("News must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Comment must not be null");
        }
        if (voteType == null) {
            throw new IllegalArgumentException("Vote type is required for a vote");
        }

        if (voteDao.findByNewsIdAndUserId(news.getId(), user.getId()).isPresent()) {
            throw new IllegalArgumentException("User has already voted on this news");
        }

        Vote vote = Vote.builder()
                .news(news)
                .user(user)
                .comment(comment)
                .voteType(voteType)
                .createdAt(LocalDateTime.now())
                .build();

        Vote saved = voteDao.save(vote);
        comment.setVote(saved);
        updateNewsStatus(news);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserVoted(Long newsId, Long userId) {
        if (newsId == null || userId == null) {
            return false;
        }
        return voteDao.findByNewsIdAndUserId(newsId, userId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countVotesByNewsIdAndType(Long newsId, VoteType voteType) {
        return voteDao.countByNewsIdAndVoteType(newsId, voteType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getFakeVotes(Long newsId) {
        return voteDao.countByNewsIdAndVoteType(newsId, VoteType.FAKE);
    }

    @Override
    @Transactional(readOnly = true)
    public long getNotFakeVotes(Long newsId) {
        return voteDao.countByNewsIdAndVoteType(newsId, VoteType.NOT_FAKE);
    }

    private void updateNewsStatus(News news) {
        long fakeVotes = voteDao.countByNewsIdAndVoteType(news.getId(), VoteType.FAKE);
        long notFakeVotes = voteDao.countByNewsIdAndVoteType(news.getId(), VoteType.NOT_FAKE);

        NewsStatus newStatus;
        if (fakeVotes == 0 && notFakeVotes == 0) {
            newStatus = NewsStatus.UNVERIFIED;
        } else if (fakeVotes > notFakeVotes) {
            newStatus = NewsStatus.FAKE;
        } else if (notFakeVotes > fakeVotes) {
            newStatus = NewsStatus.NOT_FAKE;
        } else {
            newStatus = NewsStatus.UNVERIFIED;
        }

        if (news.getStatus() != newStatus) {
            news.setStatus(newStatus);
            newsDao.save(news);
        }
    }
}
