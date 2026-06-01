package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.FileUploadResponse;
import org.ease.gamehok.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileUploadResponse> uploadFile(

            @Parameter(
                    description = "File to upload",
                    required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestParam("file")
            MultipartFile file

    ) throws Exception {

        String fileName = fileStorageService.storeFile(file);

        String downloadUrl =
                "http://localhost:8887/api/files/" + fileName;

        return ResponseEntity.ok(
                new FileUploadResponse(
                        fileName,
                        downloadUrl
                )
        );
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName
    ) throws Exception {

        Path path = Paths.get("uploads")
                .resolve(fileName)
                .normalize();

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() +
                                "\""
                )
                .body(resource);
    }
}