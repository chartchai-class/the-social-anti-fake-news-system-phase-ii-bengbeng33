import axios from "axios";
import type { Comment } from "@/types";

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
  async getComments(newsId: number) {
    return apiClient.get<Comment[]>(`/comments/${newsId}`);
  },

  async createComment(
    newsId: number,
    comment: { user?: string; text: string; imageUrl?: string }
  ) {
    return apiClient.post<Comment>("/comments", { newsId, ...comment });
  },

  async deleteComment(id: number) {
    return apiClient.delete<void>(`/comments/${id}`);
  },
};


