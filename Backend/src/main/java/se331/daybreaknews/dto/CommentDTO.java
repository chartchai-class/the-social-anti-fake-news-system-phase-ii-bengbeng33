package se331.daybreaknews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se331.daybreaknews.entity.VoteType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long newsId;
    private Long userId;
    private String username;
    private String text;
    private String imageUrl;
    private LocalDateTime createdAt;
    private VoteType voteType;
}
