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

  async createNews(news: Omit<NewsItem, "id" | "reportedAt"> & { reportedAt?: string }) {
    return apiClient.post<NewsItem>("/api/news", news);
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

