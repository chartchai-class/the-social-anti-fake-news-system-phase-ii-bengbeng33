import type { NewsItem, Comment, Vote, NewsStatus } from "frontend/src/types";

const API_BASE_URL = "http://localhost:8080/api";

// Generate a persistent user identifier
function getUserIdentifier(): string {
  let userId = localStorage.getItem("userId");
  if (!userId) {
    userId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
    localStorage.setItem("userId", userId);
  }
  return userId;
}

export const api = {
  // News API
  async getAllNews(): Promise<NewsItem[]> {
    const response = await fetch(`${API_BASE_URL}/news`);
    return response.json();
  },

  async getNewsById(id: number): Promise<NewsItem> {
    const response = await fetch(`${API_BASE_URL}/news/${id}`);
    if (!response.ok) throw new Error("News not found");
    return response.json();
  },

  async getNewsByStatus(status: string): Promise<NewsItem[]> {
    const response = await fetch(`${API_BASE_URL}/news/status/${status}`);
    return response.json();
  },

  async createNews(news: {
    title: string;
    content: string;
    reporter: string;
    imageUrl?: string;
  }): Promise<NewsItem> {
    const response = await fetch(`${API_BASE_URL}/news`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(news),
    });
    return response.json();
  },

  async updateNews(
    id: number,
    news: {
      title: string;
      content: string;
      reporter: string;
      imageUrl?: string;
    }
  ): Promise<NewsItem> {
    const response = await fetch(`${API_BASE_URL}/news/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(news),
    });
    return response.json();
  },

  async deleteNews(id: number): Promise<void> {
    await fetch(`${API_BASE_URL}/news/${id}`, {
      method: "DELETE",
    });
  },

  // Vote API
  async createVote(
    newsId: number,
    voteType: "FAKE" | "NOT_FAKE"
  ): Promise<Vote> {
    const response = await fetch(`${API_BASE_URL}/votes`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        newsId,
        voteType,
        userIdentifier: getUserIdentifier(),
      }),
    });
    if (!response.ok) throw new Error("Failed to vote or already voted");
    return response.json();
  },

  async getVoteCounts(
    newsId: number
  ): Promise<{ fake: number; notFake: number }> {
    const response = await fetch(`${API_BASE_URL}/votes/${newsId}/counts`);
    const data = await response.json();
    return {
      fake: data.fake,
      notFake: data.notFake,
    };
  },

  async hasUserVoted(newsId: number): Promise<boolean> {
    const response = await fetch(
      `${API_BASE_URL}/votes/${newsId}/has-voted?userIdentifier=${getUserIdentifier()}`
    );
    const data = await response.json();
    return data.hasVoted;
  },

  // Comment API
  async getComments(newsId: number): Promise<Comment[]> {
    const response = await fetch(`${API_BASE_URL}/comments/${newsId}`);
    return response.json();
  },

  async createComment(
    newsId: number,
    comment: { user?: string; text: string; imageUrl?: string }
  ): Promise<Comment> {
    const response = await fetch(`${API_BASE_URL}/comments`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        newsId,
        ...comment,
      }),
    });
    return response.json();
  },

  async deleteComment(id: number): Promise<void> {
    await fetch(`${API_BASE_URL}/comments/${id}`, {
      method: "DELETE",
    });
  },
};
