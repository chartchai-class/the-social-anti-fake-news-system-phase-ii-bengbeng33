import axios from "axios";

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
  async getVoteCounts(newsId: number) {
    return apiClient.get<{ fake: number; notFake: number }>(`/votes/${newsId}/counts`);
  },

  async hasUserVoted(newsId: number, userId: number) {
    return apiClient.get<{ hasVoted: boolean }>(
      `/votes/${newsId}/has-voted`,
      { params: { userId } }
    );
  },
};
