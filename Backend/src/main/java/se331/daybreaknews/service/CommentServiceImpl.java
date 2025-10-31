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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        boolean includeHidden = currentUserIsAdmin();
        List<Comment> comments = commentDao.findByNewsId(newsId);
        List<CommentDTO> output = new ArrayList<>();
        for (Comment comment : comments) {
            boolean canSee = includeHidden || comment.isVisible();
            if (canSee) {
                output.add(entityToDTO(comment));
            }
        }
        return output;
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

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsForAdmin() {
        List<Comment> comments = commentDao.findAllOrderByCreatedAtDesc();
        List<CommentDTO> output = new ArrayList<>();
        for (Comment comment : comments) {
            output.add(entityToDTO(comment));
        }
        return output;
    }

    @Override
    @Transactional
    public CommentDTO updateVisibility(Long id, boolean visible) {
        Comment comment = commentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + id));
        comment.setVisible(visible);
        Comment saved = commentDao.save(comment);
        return entityToDTO(saved);
    }

    private CommentDTO entityToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setNewsId(comment.getNews().getId());
        dto.setNewsTitle(comment.getNews().getTitle());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setText(comment.getText());
        dto.setImageUrl(comment.getImageUrl());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setVoteType(comment.getVoteType());
        dto.setVisible(comment.isVisible());
        dto.setUserVerified(comment.getUser().isVerified());
        return dto;
    }

    private boolean currentUserIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
