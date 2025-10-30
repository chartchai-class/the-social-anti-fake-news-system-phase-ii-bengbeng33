import axios from "axios";
import type { User } from "@/types";

const BASE_URL = `${import.meta.env.VITE_BACKEND_URL ?? "http://localhost:8080"}/api`;

const apiClient = axios.create({
  baseURL: BASE_URL,
  withCredentials: false,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
});

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

export default {
  async register(payload: RegisterPayload) {
    return apiClient.post<User>("/auth/register", payload);
  },

  async login(payload: LoginPayload) {
    return apiClient.post<User>("/auth/login", payload);
  },
};

