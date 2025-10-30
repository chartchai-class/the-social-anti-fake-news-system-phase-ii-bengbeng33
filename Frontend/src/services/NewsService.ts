import type { NewsItem } from "@/types";
import apiClient from "@/services/apiClient";

export default {
  async getAllNews() {
    return apiClient.get<NewsItem[]>("/api/news");
  },

  async getNewsById(id: number) {
    return apiClient.get<NewsItem>(`/api/news/${id}`);
  },

  async getNewsByStatus(status: string) {
    return apiClient.get<NewsItem[]>(`/api/news/status/${status}`);
  },

  async searchNews(query?: string) {
    const params = new URLSearchParams();
    if (query && query.trim().length > 0) params.append("q", query.trim());
    const path = params.toString().length > 0 ? `/news/search?${params.toString()}` : "/news/search";
    return apiClient.get<NewsItem[]>(path);
  },

  async createNews(news: { title: string; content: string; reporter: string; imageUrl?: string; status?: string }) {
    return apiClient.post<NewsItem>("/news", news);
  },

  async updateNews(
    id: number,
    news: Partial<Omit<NewsItem, "id">>
  ) {
    return apiClient.put<NewsItem>(`/api/news/${id}`, news);
  },

  async deleteNews(id: number) {
    return apiClient.delete<void>(`/api/news/${id}`);
  },
};

