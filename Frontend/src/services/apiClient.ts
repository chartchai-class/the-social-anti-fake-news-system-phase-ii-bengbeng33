import axios, { type InternalAxiosRequestConfig } from "axios";

const baseURL =
  import.meta.env.VITE_BACKEND_URL ?? "http://localhost:8080";

const apiClient = axios.create({
  baseURL,
});

apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  if (typeof window !== "undefined") {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${token}`;
    }
  }

  config.headers = config.headers ?? {};
  if (!config.headers.Accept) {
    config.headers.Accept = "application/json";
  }

  return config;
});

export default apiClient;
