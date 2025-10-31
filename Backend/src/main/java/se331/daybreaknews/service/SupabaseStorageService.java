package se331.daybreaknews.service;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se331.daybreaknews.dto.StorageFileDTO;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupabaseStorageService {

    @Value("${supabase.storage.bucket}")
    String bucketName;

    @Value("${supabase.storage.endpoint_output}")
    String outputUrl;

    private final S3Client s3Client;

    // Keep the formatter close to the code that uses it so we can tweak the pattern easily.
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    // Validate the file and forward it to Supabase storage.
    public StorageFileDTO uploadImage(MultipartFile file, String folder) throws ServletException, IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            throw new ServletException("Please pick a file before uploading.");
        }

        if (!fileName.contains(".")) {
            throw new ServletException("We expect an image with a proper extension.");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        List<String> allowedExt = List.of("jpg", "jpeg", "png", "gif");
        if (!allowedExt.contains(extension)) {
            throw new ServletException("File must be an image (jpg, jpeg, png, or gif).");
        }

        String targetFolder = (folder == null || folder.isBlank()) ? "images" : folder;
        String url = this.uploadFile(file, targetFolder);
        return StorageFileDTO.builder()
                .name(url)
                .build();
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String targetFolder = (folder == null || folder.isBlank()) ? "uploads" : folder;
        String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String saltedName = LocalDateTime.now().format(formatter) + "-" + originalName;
        String key = String.format("%s/%s", targetFolder, saltedName);

        Path tempFile = Files.createTempFile("upload-", originalName);

        try {
            // Copy the stream to a temp file so the SDK can read it multiple times if needed.
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }

        // Return the public URL in the same shape as the lab practice.
        return String.format("%s/%s/%s", outputUrl, bucketName, key);
    }
}
