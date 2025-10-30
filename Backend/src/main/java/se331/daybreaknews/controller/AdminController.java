package se331.daybreaknews.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se331.daybreaknews.dto.CommentDTO;
import se331.daybreaknews.dto.NewsDTO;
import se331.daybreaknews.dto.UpdateCommentVisibilityRequest;
import se331.daybreaknews.dto.UpdateNewsVisibilityRequest;
import se331.daybreaknews.dto.UpdateUserMemberRoleRequest;
import se331.daybreaknews.dto.UpdateUserVerifiedStatusRequest;
import se331.daybreaknews.dto.UserDTO;
import se331.daybreaknews.service.CommentService;
import se331.daybreaknews.service.NewsService;
import se331.daybreaknews.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final NewsService newsService;
    private final CommentService commentService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/users/{id}/member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserMemberRole(
            @PathVariable Long id,
            @RequestBody UpdateUserMemberRoleRequest request
    ) {
        return ResponseEntity.ok(userService.updateMemberRole(id, request.member()));
    }

    @PatchMapping("/users/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserVerifiedStatus(
            @PathVariable Long id,
            @RequestBody UpdateUserVerifiedStatusRequest request
    ) {
        return ResponseEntity.ok(userService.updateVerifiedStatus(id, request.verified()));
    }

    @GetMapping("/news")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNewsIncludingHidden());
    }

    @PatchMapping("/news/{id}/visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewsDTO> updateNewsVisibility(
            @PathVariable Long id,
            @RequestBody UpdateNewsVisibilityRequest request
    ) {
        return ResponseEntity.ok(newsService.updateVisibility(id, request.visible()));
    }

    @GetMapping("/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllCommentsForAdmin());
    }

    @PatchMapping("/comments/{id}/visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentDTO> updateCommentVisibility(
            @PathVariable Long id,
            @RequestBody UpdateCommentVisibilityRequest request
    ) {
        return ResponseEntity.ok(commentService.updateVisibility(id, request.visible()));
    }
}
