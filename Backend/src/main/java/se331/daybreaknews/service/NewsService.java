package se331.daybreaknews.service;

import se331.daybreaknews.dto.NewsDTO;
import se331.daybreaknews.entity.NewsStatus;
import java.util.List;

public interface NewsService {
    List<NewsDTO> getAllNews();
    List<NewsDTO> getAllNewsIncludingHidden();
    NewsDTO getNewsById(Long id);
    List<NewsDTO> getNewsByStatus(NewsStatus status);
    List<NewsDTO> searchNews(String query);
    NewsDTO createNews(NewsDTO dto);
    NewsDTO updateNews(Long id, NewsDTO dto);
    void deleteNews(Long id);
    NewsDTO updateVisibility(Long id, boolean visible);
}
