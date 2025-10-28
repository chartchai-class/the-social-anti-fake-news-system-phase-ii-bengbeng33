package se331.daybreaknews.dao;


import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;
import se331.daybreaknews.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class VoteDaoDbImpl implements VoteDao {
    final VoteRepository voteRepository;

    @Override
    public Optional<Vote> findByNewsIdAndUserIdentifier(Long newsId, String userIdentifier) {
        return voteRepository.findByNewsIdAndUserIdentifier(newsId, userIdentifier);
    }

    @Override
    public Long countByNewsIdAndVoteType(Long newsId, VoteType voteType) {
        return voteRepository.countByNewsIdAndVoteType(newsId, voteType);
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> findByNewsId(Long newsId) {
        return voteRepository.findByNewsId(newsId);
    }
}

