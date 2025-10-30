package se331.daybreaknews.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import se331.daybreaknews.config.SupabaseConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class SupabaseStorageService {
    private final SupabaseConfig supabaseConfig;
    private final WebClient webClient;

    public SupabaseStorageService(SupabaseConfig supabaseConfig) {
        this.supabaseConfig = supabaseConfig;
        this.webClient = WebClient.builder()
                .baseUrl(supabaseConfig.getUrl() + "/storage/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseConfig.getServiceKey())
                .defaultHeader("apikey", supabaseConfig.getServiceKey())
                .build();
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String uniqueFileName = folder + "/" + UUID.randomUUID().toString() + "_" 
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + extension;
            
            String uploadUrl = "/object/" + supabaseConfig.getBucketName() + "/" + uniqueFileName;
            
            byte[] fileBytes = file.getBytes();
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            
            webClient.post()
                    .uri(uploadUrl)
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("x-upsert", "true")
                    .bodyValue(fileBytes)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.info("File uploaded to Supabase: {}", uniqueFileName);
            
            return supabaseConfig.getUrl() + "/storage/v1/object/public/" 
                    + supabaseConfig.getBucketName() + "/" + uniqueFileName;
        } catch (Exception e) {
            log.error("Error uploading file to Supabase", e);
            throw new IOException("Failed to upload file to Supabase: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String path = extractPathFromUrl(fileUrl);
            if (path == null) {
                log.warn("Could not extract path from URL: {}", fileUrl);
                return;
            }
            
            String deleteUrl = "/object/" + supabaseConfig.getBucketName() + "/" + path;
            
            webClient.delete()
                    .uri(deleteUrl)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            
            log.info("File deleted from Supabase: {}", path);
        } catch (Exception e) {
            log.error("Error deleting file from Supabase", e);
        }
    }

    private String extractPathFromUrl(String url) {
        try {
            String publicPath = "/storage/v1/object/public/" + supabaseConfig.getBucketName() + "/";
            int index = url.indexOf(publicPath);
            if (index != -1) {
                return url.substring(index + publicPath.length());
            }
        } catch (Exception e) {
            log.error("Error extracting path from URL", e);
        }
        return null;
    }
}

