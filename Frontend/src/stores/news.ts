import { defineStore } from "pinia";
import NewsService from "@/services/NewsService";
import CommentService from "@/services/CommentService";
import VoteService from "@/services/VoteService";
import type { Comment, NewsItem, Status, User } from "@/types";

interface CommentData {
  text: string;
  imageUrl?: string;
  voteType: "FAKE" | "FACT";
}

function getStoredUser(): User | null {
  const raw = localStorage.getItem("user");
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw) as User;
  } catch (error) {
    console.warn("Unable to parse stored user", error);
    return null;
  }
}

function getVoteChoiceKey(newsId: number, userId: number) {
  return `voteChoice:${userId}:${newsId}`;
}

function deriveStatus(
  fakeVotes?: number,
  notFakeVotes?: number,
  fallback?: Status | "UNVERIFIED" | null
): Status {
  if (fakeVotes == null || notFakeVotes == null) {
    return (fallback as Status) ?? null;
  }
  if (fakeVotes === 0 && notFakeVotes === 0) {
    return "UNVERIFIED";
  }
  if (fakeVotes > notFakeVotes) {
    return "FAKE";
  }
  if (notFakeVotes > fakeVotes) {
    return "FACT";
  }
  return "UNVERIFIED";
}

interface NewNewsData {
  title: string;
  content: string;
  reporter: string;
  imageUrl?: string;
  status?: Status;
}

export const useNewsStore = defineStore("news", {
  state: () => ({
    // All news data from API
    allNews: [] as NewsItem[],

    // Cache for user votes (to avoid repeated API calls)
    userVotes: new Set<number>(),

    // Cache for comments
    commentsCache: new Map<number, Comment[]>(),

    // Loading states
    isLoading: false,
    authSyncInitialized: false,
  }),

  getters: {
    // Get all news
    getAllNews: (state): NewsItem[] => {
      return state.allNews;
    },

    // Get news by ID
    getNewsById:
      (state) =>
      (id: string | number): NewsItem | undefined => {
        const idNum = Number.parseInt(id.toString());
        return state.allNews.find((news) => news.id === idNum);
      },

    // Get current vote counts for a news item
    getVoteCounts:
      (state) =>
      async (
        newsId: string | number
      ): Promise<{ fake: number; notFake: number }> => {
        const id = Number.parseInt(newsId.toString());
        try {
          const { data } = await VoteService.getVoteCounts(id);
          return { fake: data.fake, notFake: data.notFake };
        } catch (error) {
          console.error("Error fetching vote counts:", error);
          return { fake: 0, notFake: 0 };
        }
      },

    // Check if user has voted on specific news
    hasUserVoted:
      (state) =>
      (newsId: string | number): boolean => {
        const id = Number.parseInt(newsId.toString());
        return state.userVotes.has(id);
      },

    // Get comments for specific news
    getComments:
      (state) =>
      async (newsId: string | number): Promise<Comment[]> => {
        const id = Number.parseInt(newsId.toString());
        if (state.commentsCache.has(id)) {
          return state.commentsCache.get(id) || [];
        }
        try {
          const { data: comments } = await CommentService.getComments(id);
          state.commentsCache.set(id, comments);
          return comments;
        } catch (error) {
          console.error("Error fetching comments:", error);
          return [];
        }
      },

    // Get current status based on vote counts
    getCurrentStatus:
      (state) =>
      (newsId: string | number): Status => {
        const id = Number.parseInt(newsId.toString());
        const news = state.allNews.find((n) => n.id === id);
        return deriveStatus(
          news?.fakeVotes,
          news?.notFakeVotes,
          news?.status ?? null
        );
      },

    // Filter news by status
    getNewsByStatus:
      (state) =>
      (status: string): NewsItem[] => {
        if (status === "all") return state.allNews;
        return state.allNews.filter((news) => news.status === status);
      },

    // Get news with updated vote counts
    getNewsWithCurrentVotes: (state): NewsItem[] => {
      return state.allNews.map((news) => ({
        ...news,
        currentStatus: deriveStatus(
          news.fakeVotes,
          news.notFakeVotes,
          news.status ?? null
        ),
      }));
    },
  },

  actions: {
    initializeAuthSync() {
      if (typeof window === "undefined") {
        return;
      }
      if (this.authSyncInitialized) return;
      this.authSyncInitialized = true;
      globalThis.window.addEventListener("auth-changed", () => {
        this.userVotes.clear();
        this.clearCache();
      });
      globalThis.window.addEventListener(
        "admin-comment-updated",
        (event: Event) => {
          const detail = (event as CustomEvent<{ newsId?: number }>).detail;
          if (detail?.newsId != null) {
            this.commentsCache.delete(detail.newsId);
            this.fetchNewsById(detail.newsId).catch((error) => {
              console.error("Error refreshing news after comment update", error);
            });
          }
        },
      );
    },

    // Fetch all news from API
    async fetchAllNews() {
      this.isLoading = true;
      try {
        const { data } = await NewsService.getAllNews();
        this.allNews = data;
      } catch (error) {
        console.error("Error fetching news:", error);
      } finally {
        this.isLoading = false;
      }
    },

    async searchNews(query?: string) {
      this.isLoading = true;
      try {
        const { data } = await NewsService.searchNews(query);
        this.allNews = data;
      } catch (error) {
        console.error("Error searching news:", error);
      } finally {
        this.isLoading = false;
      }
    },

    // Fetch news by ID from API
    async fetchNewsById(id: string | number) {
      const idNum = Number.parseInt(id.toString());
      try {
        const { data: news } = await NewsService.getNewsById(idNum);
        const index = this.allNews.findIndex((n) => n.id === news.id);
        if (index >= 0) {
          this.allNews[index] = news;
        } else {
          this.allNews.push(news);
        }
        return news;
      } catch (error) {
        console.error("Error fetching news:", error);
        return undefined;
      }
    },

    // Add new news item
    async addNews(newsData: NewNewsData) {
      try {
        const payload = {
          title: newsData.title,
          content: newsData.content,
          reporter: newsData.reporter,
          imageUrl: newsData.imageUrl,
          ...(newsData.status ? { status: newsData.status as string } : {}),
        };
        const { data: newNews } = await NewsService.createNews(payload);
        this.allNews.unshift(newNews);
        return newNews;
      } catch (error) {
        console.error("Error creating news:", error);
        throw error;
      }
    },

    async refreshUserVoteStatus(newsId: string | number): Promise<boolean> {
      const id = Number.parseInt(newsId.toString());
      const user = getStoredUser();

      if (!user) {
        this.userVotes.delete(id);
        return false;
      }

      try {
        const { data } = await VoteService.hasUserVoted(id, user.id);
        if (data.hasVoted) {
          this.userVotes.add(id);
        } else {
          this.userVotes.delete(id);
          localStorage.removeItem(getVoteChoiceKey(id, user.id));
        }
        return data.hasVoted;
      } catch (error) {
        console.error("Error checking vote status:", error);
        return this.userVotes.has(id);
      }
    },

    // Add comment to news
    async addComment(
      newsId: string | number,
      commentData: CommentData
    ): Promise<Comment> {
      const id = Number.parseInt(newsId.toString());
      const user = getStoredUser();

      if (!user) {
        throw new Error("User must be logged in to comment");
      }

      try {
        const { data: newComment } = await CommentService.createComment(id, {
          userId: user.id,
          ...commentData,
        });

        this.userVotes.add(id);
        localStorage.setItem(
          getVoteChoiceKey(id, user.id),
          commentData.voteType
        );

        await this.fetchNewsById(id);

        // Update cache
        const comments = this.commentsCache.get(id) || [];
        comments.unshift(newComment);
        this.commentsCache.set(id, comments);

        return newComment;
      } catch (error) {
        console.error("Error adding comment:", error);
        throw error;
      }
    },

    // Clear cache
    clearCache() {
      this.commentsCache.clear();
    },
  },
});
