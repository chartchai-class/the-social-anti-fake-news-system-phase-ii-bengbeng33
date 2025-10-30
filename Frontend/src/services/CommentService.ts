import type { Comment } from "@/types";
import apiClient from "@/services/apiClient";

export default {
  async getComments(newsId: number) {
    return apiClient.get<Comment[]>(`/api/comments/${newsId}`);
  },

  async createComment(
    newsId: number,
    comment: {
      userId: number;
      text: string;
      imageUrl?: string;
      voteType: "FAKE" | "FACT";
    }
  ) {
    return apiClient.post<Comment>("/api/comments", { newsId, ...comment });
  },

  async deleteComment(id: number) {
    return apiClient.delete<void>(`/api/comments/${id}`);
  },
};
