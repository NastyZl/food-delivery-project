package ru.nastyzl.fooddelivery.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for file upload operations.
 */
public class FileUploadUtil {

    /**
     * Saves the uploaded file to the specified directory.
     *
     * @param uploadDir     the directory to save the file
     * @param fileName      name of file
     * @param multipartFile uploaded file
     * @throws IOException if I/O error occurs
     */
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Не можем сохранить файл: " + fileName, e);
        }
    }
}