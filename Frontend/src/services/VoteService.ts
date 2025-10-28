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

function getUserIdentifier(): string {
  let userId = localStorage.getItem("userId");
  if (!userId) {
    userId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
    localStorage.setItem("userId", userId);
  }
  return userId;
}

export default {
  async createVote(newsId: number, voteType: "FAKE" | "NOT_FAKE") {
    return apiClient.post("/votes", {
      newsId,
      voteType,
      userIdentifier: getUserIdentifier(),
    });
  },

  async getVoteCounts(newsId: number) {
    return apiClient.get<{ fake: number; notFake: number }>(`/votes/${newsId}/counts`);
  },

  async hasUserVoted(newsId: number) {
    return apiClient.get<{ hasVoted: boolean }>(
      `/votes/${newsId}/has-voted`,
      { params: { userIdentifier: getUserIdentifier() } }
    );
  },
};


