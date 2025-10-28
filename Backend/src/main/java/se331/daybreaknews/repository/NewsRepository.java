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
}