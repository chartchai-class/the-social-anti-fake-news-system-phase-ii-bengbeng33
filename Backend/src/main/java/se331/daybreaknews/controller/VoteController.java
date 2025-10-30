package se331.daybreaknews.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se331.daybreaknews.service.VoteService;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {
    final VoteService voteService;

    @GetMapping("/{newsId}/counts")
    public ResponseEntity<Map<String, Long>> getVoteCounts(@PathVariable Long newsId) {
        long fakeVotes = voteService.getFakeVotes(newsId);
        long notFakeVotes = voteService.getNotFakeVotes(newsId);

        return ResponseEntity.ok(Map.of(
                "fake", fakeVotes,
                "notFake", notFakeVotes
        ));
    }

    @GetMapping("/{newsId}/has-voted")
    public ResponseEntity<Map<String, Boolean>> hasUserVoted(@PathVariable Long newsId, @RequestParam Long userId) {
        boolean hasVoted = voteService.hasUserVoted(newsId, userId);

        return ResponseEntity.ok(Map.of("hasVoted", hasVoted));
    }
}
