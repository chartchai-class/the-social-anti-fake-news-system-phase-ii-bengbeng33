package se331.daybreaknews.controller;

import se331.daybreaknews.dto.NewsDTO;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(newsService.getNewsById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NewsDTO>> getNewsByStatus(@PathVariable NewsStatus status) {
        return ResponseEntity.ok(newsService.getNewsByStatus(status));
    }
}
