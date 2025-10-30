import axios from "axios";

const BASE_URL = `${import.meta.env.VITE_BACKEND_URL ?? "http://localhost:8080"}/api`;

const apiClient = axios.create({
  baseURL: BASE_URL,
  withCredentials: false,
  headers: {
    Accept: "application/json",
  },
});

export interface UploadResponse {
  url: string;
  message: string;
}

export default {
  async uploadImage(file: File, folder: string = "images"): Promise<string> {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("folder", folder);

    const response = await apiClient.post<UploadResponse>(
      "/upload/image",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );

    return response.data.url;
  },

  async uploadFile(file: File, folder: string = "files"): Promise<string> {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("folder", folder);

    const response = await apiClient.post<UploadResponse>(
      "/upload/file",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );

    return response.data.url;
  },

  async deleteFile(fileUrl: string): Promise<void> {
    await apiClient.delete("/upload", {
      params: { url: fileUrl },
    });
  },
};

