import apiClient from "@/services/apiClient";

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
      "/api/upload/image",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );

    return response.data.url;
  },
};
