package se331.daybreaknews.service;

import se331.daybreaknews.dao.NewsDao;
import se331.daybreaknews.dao.VoteDao;
import se331.daybreaknews.dto.NewsDTO;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.entity.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    final NewsDao newsDao;
    final VoteDao voteDao;

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getAllNews() {
        return newsDao.findAllByOrderByReportedAtDesc()
                .stream()
                .map(this::enrichWithVotes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getNewsByStatus(NewsStatus status) {
        return newsDao.findByStatus(status)
                .stream()
                .map(this::enrichWithVotes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> searchNews(String query) {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) {
            return getAllNews();
        }

        // Search title/content first
        List<News> results = newsDao
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q);

        // If query equals a status word, include status results as well
        NewsStatus status = null;
        for (NewsStatus s : NewsStatus.values()) {
            if (s.name().equalsIgnoreCase(q)) {
                status = s;
                break;
            }
        }
        if (status != null) {
            List<News> byStatus = newsDao.findByStatus(status);
            for (News n : byStatus) {
                if (results.stream().noneMatch(x -> x.getId().equals(n.getId()))) {
                    results.add(n);
                }
            }
        }

        return results.stream().map(this::enrichWithVotes).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        News news = newsDao.getNews(id);
        if (news == null) {
            throw new RuntimeException("News not found with id: " + id);
        }
        return enrichWithVotes(news);
    }

    @Override
    @Transactional
    public NewsDTO createNews(NewsDTO dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setReporter(dto.getReporter());
        news.setImageUrl(dto.getImageUrl());
        news.setStatus(NewsStatus.UNVERIFIED);
        news.setReportedAt(LocalDateTime.now());
        
        if (dto.getSummary() == null || dto.getSummary().isEmpty()) {
            news.setSummary(dto.getContent().length() > 100 ? 
                dto.getContent().substring(0, 100) + "..." : dto.getContent());
        } else {
            news.setSummary(dto.getSummary());
        }
        
        News saved = newsDao.save(news);
        return enrichWithVotes(saved);
    }

    @Override
    @Transactional
    public NewsDTO updateNews(Long id, NewsDTO dto) {
        News news = newsDao.getNews(id);
        if (news == null) {
            throw new RuntimeException("News not found with id: " + id);
        }
        
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setReporter(dto.getReporter());
        news.setSummary(dto.getSummary());
        if (dto.getImageUrl() != null) {
            news.setImageUrl(dto.getImageUrl());
        }
        if (dto.getStatus() != null) {
            news.setStatus(dto.getStatus());
        }
        
        News saved = newsDao.save(news);
        return enrichWithVotes(saved);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        newsDao.deleteNews(id);
    }

    private NewsDTO enrichWithVotes(News news) {
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        long fakeVotes = voteDao.countByNewsIdAndVoteType(news.getId(), VoteType.FAKE);
        long notFakeVotes = voteDao.countByNewsIdAndVoteType(news.getId(), VoteType.FACT);

        dto.setStatus(resolveStatus(news, fakeVotes, notFakeVotes));
        dto.setReporter(news.getReporter());
        dto.setReportedAt(news.getReportedAt());
        dto.setImageUrl(news.getImageUrl());
        dto.setFakeVotes(fakeVotes);
        dto.setNotFakeVotes(notFakeVotes);
        
        return dto;
    }

    private NewsStatus resolveStatus(News news, long fakeVotes, long notFakeVotes) {
        if (fakeVotes == 0 && notFakeVotes == 0) {
            return NewsStatus.UNVERIFIED;
        }
        if (fakeVotes > notFakeVotes) {
            return NewsStatus.FAKE;
        }
        if (notFakeVotes > fakeVotes) {
            return NewsStatus.FACT;
        }
        return NewsStatus.UNVERIFIED;
    }
}
