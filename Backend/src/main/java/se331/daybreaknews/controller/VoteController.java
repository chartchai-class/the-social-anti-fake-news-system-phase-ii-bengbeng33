package se331.daybreaknews.controller;

import se331.daybreaknews.dto.VoteDTO;
import se331.daybreaknews.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteDTO> createVote(@Valid @RequestBody VoteDTO dto) {
        try {
            VoteDTO vote = voteService.createVote(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(vote);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{newsId}/counts")
    public ResponseEntity<Map<String, Long>> getVoteCounts(@PathVariable Long newsId) {
        long fakeVotes = voteService.getFakeVotes(newsId);
        long notFakeVotes = voteService.getNotFakeVotes(newsId);
        
        Map<String, Long> counts = new HashMap<>();
        counts.put("fake", fakeVotes);
        counts.put("notFake", notFakeVotes);
        
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/{newsId}/has-voted")
    public ResponseEntity<Map<String, Boolean>> hasUserVoted(@PathVariable Long newsId, @RequestParam String userIdentifier) {
        boolean hasVoted = voteService.hasUserVoted(newsId, userIdentifier);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasVoted", hasVoted);
        
        return ResponseEntity.ok(response);
    }
}