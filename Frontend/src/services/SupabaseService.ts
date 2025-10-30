import { createClient } from "@supabase/supabase-js";

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || "";
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY || "";

export const supabase = createClient(supabaseUrl, supabaseAnonKey);

export default {
  async uploadFile(file: File, bucket: string = "news-images", folder: string = "images"): Promise<string> {
    try {
      const fileExt = file.name.split(".").pop();
      const fileName = `${folder}/${Math.random().toString(36).substring(2)}_${Date.now()}.${fileExt}`;
      
      const { error } = await supabase.storage
        .from(bucket)
        .upload(fileName, file, {
          cacheControl: "3600",
          upsert: false,
        });

      if (error) {
        throw error;
      }

      // Get public URL
      const { data: urlData } = supabase.storage
        .from(bucket)
        .getPublicUrl(fileName);

      return urlData.publicUrl;
    } catch (error) {
      console.error("Error uploading file to Supabase:", error);
      throw error;
    }
  },

  async deleteFile(fileUrl: string, bucket: string = "news-images"): Promise<void> {
    try {
      // Extract file path from URL
      const url = new URL(fileUrl);
      const pathParts = url.pathname.split("/");
      const bucketIndex = pathParts.indexOf(bucket);
      if (bucketIndex === -1 || bucketIndex === pathParts.length - 1) {
        throw new Error("Invalid file URL");
      }
      const filePath = pathParts.slice(bucketIndex + 1).join("/");

      const { error } = await supabase.storage
        .from(bucket)
        .remove([filePath]);

      if (error) {
        throw error;
      }
    } catch (error) {
      console.error("Error deleting file from Supabase:", error);
      throw error;
    }
  },
};

