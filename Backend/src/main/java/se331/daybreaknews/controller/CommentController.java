package se331.daybreaknews.controller;

import se331.daybreaknews.dto.CommentDTO;
import se331.daybreaknews.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    final CommentService commentService;

    @GetMapping("/{newsId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByNewsId(@PathVariable Long newsId) {
        List<CommentDTO> comments = commentService.getCommentsByNewsId(newsId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO dto) {
        CommentDTO comment = commentService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

