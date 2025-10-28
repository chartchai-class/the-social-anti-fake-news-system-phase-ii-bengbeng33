import { defineStore } from "pinia";
import NewsService from "@/services/NewsService";
import CommentService from "@/services/CommentService";
import VoteService from "@/services/VoteService";

type Status = "FAKE" | "NOT_FAKE" | null;

interface NewsItem {
  id: number;
  title: string;
  summary?: string;
  content: string;
  status?: Status;
  reporter: string;
  reportedAt: string;
  imageUrl?: string;
  fakeVotes?: number;
  notFakeVotes?: number;
}

interface Comment {
  id: number;
  user: string;
  text: string;
  imageUrl?: string;
  createdAt: string;
}

interface CommentData {
  text: string;
  imageUrl?: string;
  user?: string;
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
        return news?.status || null;
      },

    // Filter news by status
    getNewsByStatus:
      (state) =>
      (status: string): NewsItem[] => {
        if (status === "all") return state.allNews;
        return state.allNews.filter((news) => news.status === status);
      },

    // Get news with updated vote counts
    getNewsWithCurrentVotes: (state) => (): NewsItem[] => {
      return state.allNews.map((news) => ({
        ...news,
        currentStatus: news.status,
      }));
    },
  },

  actions: {
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
        const { data: newNews } = await NewsService.createNews(newsData as any);
        this.allNews.unshift(newNews);
        return newNews;
      } catch (error) {
        console.error("Error creating news:", error);
        throw error;
      }
    },

    // Vote on a news item
    async voteOnNews(
      newsId: string | number,
      voteType: Status
    ): Promise<boolean> {
      const id = Number.parseInt(newsId.toString());

      // Check if user already voted
      if (this.userVotes.has(id)) {
        console.warn("User has already voted on this news");
        return false;
      }

      try {
        await VoteService.createVote(id, voteType as "FAKE" | "NOT_FAKE");
        this.userVotes.add(id);

        // Refresh the news item to get updated vote counts
        await this.fetchNewsById(id);

        return true;
      } catch (error) {
        console.error("Error voting:", error);
        return false;
      }
    },

    // Add comment to news
    async addComment(
      newsId: string | number,
      commentData: CommentData
    ): Promise<Comment> {
      const id = Number.parseInt(newsId.toString());

      try {
        const { data: newComment } = await CommentService.createComment(id, commentData);

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
