package se331.daybreaknews.service;

import se331.daybreaknews.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByNewsId(Long newsId);
    CommentDTO createComment(CommentDTO dto);
    void deleteComment(Long id);
}
