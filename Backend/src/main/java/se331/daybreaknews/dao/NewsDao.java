package se331.daybreaknews.dao;

import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import java.util.List;

public interface NewsDao {
    News getNews(Long id);
    List<News> findByStatus(NewsStatus status);
    List<News> findAllByOrderByReportedAtDesc();
    List<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String q1, String q2);
    List<News> findByStatusAndTitleContainingIgnoreCase(NewsStatus status, String q);
    List<News> findByStatusAndContentContainingIgnoreCase(NewsStatus status, String q);
    News save(News news);
    void deleteNews(Long id);
    News updateNews(News news);
}