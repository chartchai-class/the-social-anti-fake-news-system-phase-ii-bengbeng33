package se331.daybreaknews.repository;

import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByStatus(NewsStatus status);
    List<News> findAllByOrderByReportedAtDesc();

    // Simple contains search (case-insensitive) on title or content
    List<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

    // Search by status and title
    List<News> findByStatusAndTitleContainingIgnoreCase(NewsStatus status, String title);

    // Search by status and content
    List<News> findByStatusAndContentContainingIgnoreCase(NewsStatus status, String content);
}