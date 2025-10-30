package se331.daybreaknews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long newsId;
    private String username;
    private String text;
    private String imageUrl;
    private LocalDateTime createdAt;
}
