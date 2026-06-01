package org.ease.gamehok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String fileDownloadUri;
}

