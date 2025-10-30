package se331.daybreaknews.dto;

import se331.daybreaknews.entity.NewsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private NewsStatus status;
    private String reporter;
    private LocalDateTime reportedAt;
    private String imageUrl;
    private Long fakeVotes;
    private Long notFakeVotes;
    private boolean visible;
}
