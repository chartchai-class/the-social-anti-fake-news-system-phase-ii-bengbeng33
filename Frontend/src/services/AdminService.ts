import type { Comment, NewsItem, User } from "@/types";
import apiClient from "@/services/apiClient";

interface ToggleMemberPayload {
  member: boolean;
}

interface UpdateVerifiedPayload {
  verified: boolean;
}

interface UpdateVisibilityPayload {
  visible: boolean;
}

export default {
  async getAllUsers() {
    return apiClient.get<User[]>("/api/admin/users");
  },

  async updateUserMember(id: number, makeMember: boolean) {
    const payload: ToggleMemberPayload = { member: makeMember };
    return apiClient.patch<User>(`/api/admin/users/${id}/member`, payload);
  },

  async updateUserVerified(id: number, verified: boolean) {
    const payload: UpdateVerifiedPayload = { verified };
    return apiClient.patch<User>(`/api/admin/users/${id}/verify`, payload);
  },

  async getAllNews() {
    return apiClient.get<NewsItem[]>("/api/admin/news");
  },

  async updateNewsVisibility(id: number, visible: boolean) {
    const payload: UpdateVisibilityPayload = { visible };
    return apiClient.patch<NewsItem>(`/api/admin/news/${id}/visibility`, payload);
  },

  async getAllComments() {
    return apiClient.get<Comment[]>("/api/admin/comments");
  },

  async updateCommentVisibility(id: number, visible: boolean) {
    const payload: UpdateVisibilityPayload = { visible };
    return apiClient.patch<Comment>(`/api/admin/comments/${id}/visibility`, payload);
  },
};
