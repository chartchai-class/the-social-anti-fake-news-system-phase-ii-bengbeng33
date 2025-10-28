import axios from "axios";
import type { NewsItem } from "@/types";

const BASE_URL = `${import.meta.env.VITE_BACKEND_URL ?? "http://localhost:8080"}/api`;

const apiClient = axios.create({
  baseURL: BASE_URL,
  withCredentials: false,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
});

export default {
  async getAllNews() {
    return apiClient.get<NewsItem[]>("/news");
  },

  async getNewsById(id: number) {
    return apiClient.get<NewsItem>(`/news/${id}`);
  },

  async getNewsByStatus(status: string) {
    return apiClient.get<NewsItem[]>(`/news/status/${status}`);
  },

  async createNews(news: Omit<NewsItem, "id" | "reportedAt"> & { reportedAt?: string }) {
    return apiClient.post<NewsItem>("/news", news);
  },

  async updateNews(
    id: number,
    news: Partial<Omit<NewsItem, "id">>
  ) {
    return apiClient.put<NewsItem>(`/news/${id}`, news);
  },

  async deleteNews(id: number) {
    return apiClient.delete<void>(`/news/${id}`);
  },
};


