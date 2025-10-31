package se331.daybreaknews.service;

import se331.daybreaknews.dao.NewsDao;
import se331.daybreaknews.dao.UserDao;
import se331.daybreaknews.dao.VoteDao;
import se331.daybreaknews.dto.NewsDTO;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.entity.User;
import se331.daybreaknews.entity.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    final NewsDao newsDao;
    final VoteDao voteDao;
    final UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getAllNews() {
        boolean includeHidden = currentUserIsAdmin();
        List<News> allNews = newsDao.findAllByOrderByReportedAtDesc();
        List<NewsDTO> output = new ArrayList<>();
        for (News news : allNews) {
            boolean canSee = includeHidden || news.isVisible();
            if (canSee) {
                output.add(enrichWithVotes(news));
            }
        }
        return output;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getAllNewsIncludingHidden() {
        List<News> allNews = newsDao.findAllByOrderByReportedAtDesc();
        List<NewsDTO> output = new ArrayList<>();
        for (News news : allNews) {
            output.add(enrichWithVotes(news));
        }
        return output;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getNewsByStatus(NewsStatus status) {
        boolean includeHidden = currentUserIsAdmin();
        List<News> newsByStatus = newsDao.findByStatus(status);
        List<NewsDTO> output = new ArrayList<>();
        for (News news : newsByStatus) {
            boolean canSee = includeHidden || news.isVisible();
            if (canSee) {
                output.add(enrichWithVotes(news));
            }
        }
        return output;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> searchNews(String query) {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) {
            return getAllNews();
        }

        List<News> matchedNews = new ArrayList<>(
                newsDao.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q)
        );

        // Allow searching by status keyword as well.
        NewsStatus matchedStatus = null;
        for (NewsStatus statusCandidate : NewsStatus.values()) {
            if (statusCandidate.name().equalsIgnoreCase(q)) {
                matchedStatus = statusCandidate;
                break;
            }
        }
        if (matchedStatus != null) {
            List<News> byStatus = newsDao.findByStatus(matchedStatus);
            for (News candidate : byStatus) {
                boolean alreadyAdded = false;
                for (News existing : matchedNews) {
                    if (existing.getId().equals(candidate.getId())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded) {
                    matchedNews.add(candidate);
                }
            }
        }

        boolean includeHidden = currentUserIsAdmin();
        List<NewsDTO> output = new ArrayList<>();
        for (News news : matchedNews) {
            boolean canSee = includeHidden || news.isVisible();
            if (canSee) {
                output.add(enrichWithVotes(news));
            }
        }
        return output;
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        News news = newsDao.getNews(id);
        if (news == null || (!news.isVisible() && !currentUserIsAdmin())) {
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
        news.setVisible(true);
        
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
    public NewsDTO updateVisibility(Long id, boolean visible) {
        News news = newsDao.getNews(id);
        if (news == null) {
            throw new RuntimeException("News not found with id: " + id);
        }
        news.setVisible(visible);
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
        String reporter = news.getReporter();
        dto.setReporter(reporter);
        boolean reporterVerified = false;
        if (reporter != null && !reporter.isBlank()) {
            reporterVerified = userDao.findByUsername(reporter)
                    .map(User::isVerified)
                    .orElse(false);
        }
        dto.setReporterVerified(reporterVerified);
        dto.setReportedAt(news.getReportedAt());
        dto.setImageUrl(news.getImageUrl());
        dto.setFakeVotes(fakeVotes);
        dto.setNotFakeVotes(notFakeVotes);
        dto.setVisible(news.isVisible());
        
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

    private boolean currentUserIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
