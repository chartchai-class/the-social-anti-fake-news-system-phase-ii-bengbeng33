package se331.daybreaknews.controller;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se331.daybreaknews.dto.StorageFileDTO;
import se331.daybreaknews.service.SupabaseStorageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {
    
    private final SupabaseStorageService supabaseStorageService;

    @PostMapping("/image")
    // receive image from the browser and forward to supabase
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "images") String folder) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Please choose a file before uploading."));
            }

            StorageFileDTO uploaded = supabaseStorageService.uploadImage(file, folder);
            return ResponseEntity.ok(uploaded);
        } catch (ServletException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error uploading file: " + e.getMessage()));
        }
    }
}
