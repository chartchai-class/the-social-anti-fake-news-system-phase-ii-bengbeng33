import apiClient from "@/services/apiClient";

export interface UploadResponse {
  name: string;
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

    // The backend now follows the same pattern as the lab sheet and returns { name: "<url>" }.
    return response.data.name;
  },
};
