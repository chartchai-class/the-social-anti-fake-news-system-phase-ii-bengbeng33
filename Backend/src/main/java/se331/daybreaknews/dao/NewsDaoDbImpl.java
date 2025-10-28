package se331.daybreaknews.dao;

import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class NewsDaoDbImpl implements NewsDao {
    final NewsRepository newsRepository;

    @Override
    public News getNews(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public List<News> findByStatus(NewsStatus status) {
        return newsRepository.findByStatus(status);
    }

    @Override
    public List<News> findAllByOrderByReportedAtDesc() {
        return newsRepository.findAllByOrderByReportedAtDesc();
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }
    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}