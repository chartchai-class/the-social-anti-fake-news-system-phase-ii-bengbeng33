import apiClient from "@/services/apiClient";

export default {
  async getVoteCounts(newsId: number) {
    return apiClient.get<{ fake: number; notFake: number }>(`/api/votes/${newsId}/counts`);
  },

  async hasUserVoted(newsId: number, userId: number) {
    return apiClient.get<{ hasVoted: boolean }>(
      `/api/votes/${newsId}/has-voted`,
      { params: { userId } }
    );
  },
};
