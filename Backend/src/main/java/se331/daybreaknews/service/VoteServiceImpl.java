package se331.daybreaknews.service;

import se331.daybreaknews.dao.VoteDao;
import se331.daybreaknews.dto.VoteDTO;
import se331.daybreaknews.entity.Vote;
import se331.daybreaknews.entity.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    final VoteDao voteDao;

    @Override
    @Transactional
    public VoteDTO createVote(VoteDTO dto) {
        if (dto.getUserIdentifier() == null || dto.getUserIdentifier().isEmpty()) {
            dto.setUserIdentifier(UUID.randomUUID().toString());
        }
        
        boolean hasVoted = voteDao.findByNewsIdAndUserIdentifier(
            dto.getNewsId(), dto.getUserIdentifier()).isPresent();
        
        if (hasVoted) {
            throw new RuntimeException("User has already voted on this news");
        }
        
        Vote vote = new Vote();
        vote.setNewsId(dto.getNewsId());
        vote.setVoteType(dto.getVoteType());
        vote.setUserIdentifier(dto.getUserIdentifier());
        
        Vote saved = voteDao.save(vote);
        return entityToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserVoted(Long newsId, String userIdentifier) {
        return voteDao.findByNewsIdAndUserIdentifier(newsId, userIdentifier).isPresent();
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

    private VoteDTO entityToDTO(Vote vote) {
        VoteDTO dto = new VoteDTO();
        dto.setNewsId(vote.getNewsId());
        dto.setVoteType(vote.getVoteType());
        dto.setUserIdentifier(vote.getUserIdentifier());
        return dto;
    }
}