package se331.daybreaknews.dto;

import se331.daybreaknews.entity.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long newsId;
    private VoteType voteType;
    private String userIdentifier;
}

