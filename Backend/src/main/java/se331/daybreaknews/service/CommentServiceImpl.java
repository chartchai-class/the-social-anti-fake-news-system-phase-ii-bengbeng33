package se331.daybreaknews.service;

import se331.daybreaknews.dao.CommentDao;
import se331.daybreaknews.dto.CommentDTO;
import se331.daybreaknews.entity.Comment;
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
        Comment comment = new Comment();
        comment.setNewsId(dto.getNewsId());
        comment.setUsername(dto.getUsername());
        comment.setText(dto.getText());
        comment.setImageUrl(dto.getImageUrl());
        comment.setCreatedAt(LocalDateTime.now());
        
        Comment saved = commentDao.save(comment);
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
        dto.setNewsId(comment.getNewsId());
        dto.setUsername(comment.getUsername());
        dto.setText(comment.getText());
        dto.setImageUrl(comment.getImageUrl());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
