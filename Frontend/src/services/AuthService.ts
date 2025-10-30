import type { User } from "@/types";
import apiClient from "@/services/apiClient";

export interface RegisterPayload {
  name: string;
  surname: string;
  email: string;
  password: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export default {
  async register(payload: RegisterPayload) {
    return apiClient.post<AuthResponse>("/api/v1/auth/register", payload);
  },

  async login(payload: LoginPayload) {
    return apiClient.post<AuthResponse>("/api/v1/auth/authenticate", payload);
  },
};
