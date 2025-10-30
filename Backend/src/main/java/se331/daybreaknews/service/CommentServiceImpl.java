package se331.daybreaknews.service;

import se331.daybreaknews.dao.CommentDao;
import se331.daybreaknews.dao.NewsDao;
import se331.daybreaknews.dao.UserDao;
import se331.daybreaknews.dto.CommentDTO;
import se331.daybreaknews.entity.Comment;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.entity.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final CommentDao commentDao;
    final NewsDao newsDao;
    final UserDao userDao;
    final VoteService voteService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByNewsId(Long newsId) {
        return commentDao.findByNewsId(newsId)
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO dto) {
        if (dto.getVoteType() == null) {
            throw new IllegalArgumentException("Vote type is required for a comment");
        }
        if (dto.getNewsId() == null) {
            throw new IllegalArgumentException("News id is required for a comment");
        }
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User id is required for a comment");
        }

        News news = newsDao.getNews(dto.getNewsId());
        if (news == null) {
            throw new IllegalArgumentException("News not found: " + dto.getNewsId());
        }

        User user = userDao.getUser(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));

        if (voteService.hasUserVoted(news.getId(), user.getId())) {
            throw new IllegalArgumentException("User has already voted on this news");
        }

        Comment comment = new Comment();
        comment.setNews(news);
        comment.setUser(user);
        comment.setText(dto.getText());
        comment.setImageUrl(dto.getImageUrl());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setVoteType(dto.getVoteType());

        Comment saved = commentDao.save(comment);

        Vote vote = voteService.createVoteForComment(news, user, saved, dto.getVoteType());
        saved.setVote(vote);

        return entityToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentDao.deleteComment(id);
    }

    private CommentDTO entityToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setNewsId(comment.getNews().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setText(comment.getText());
        dto.setImageUrl(comment.getImageUrl());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setVoteType(comment.getVoteType());
        return dto;
    }
}
