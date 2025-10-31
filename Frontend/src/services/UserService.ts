import apiClient from "./apiClient";
import type { UserProfile } from "@/types";

export default {
  async getMyProfile(): Promise<UserProfile> {
    const response = await apiClient.get<UserProfile>("/api/users/me/profile");
    return response.data;
  },
};

