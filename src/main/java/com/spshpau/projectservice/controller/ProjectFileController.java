package com.spshpau.projectservice.controller;

import com.spshpau.projectservice.dto.FileDownloadDto;
import com.spshpau.projectservice.dto.ProjectFileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProjectFileController {

    ResponseEntity<ProjectFileResponseDto> uploadProjectFile(@PathVariable UUID projectId,
                                                             @RequestPart("file") MultipartFile file,
                                                             @RequestPart(value = "description", required = false) String description,
                                                             Jwt jwt) throws IOException;

    ResponseEntity<List<ProjectFileResponseDto>> getProjectFiles(@PathVariable UUID projectId,
                                                                 Jwt jwt);

    ResponseEntity<ProjectFileResponseDto> getProjectFileMetadata(@PathVariable UUID projectId,
                                                                  @PathVariable UUID fileId,
                                                                  Jwt jwt);

    ResponseEntity<FileDownloadDto> getProjectFileDownloadUrl(@PathVariable UUID projectId,
                                                              @PathVariable UUID fileId,
                                                              Jwt jwt);

    ResponseEntity<Void> deleteProjectFile(@PathVariable UUID projectId,
                                           @PathVariable UUID fileId,
                                           Jwt jwt);

    ResponseEntity<List<ProjectFileResponseDto>> getAllVersionsOfFileByName(
            @PathVariable UUID projectId,
            @RequestParam("filename") String originalFilename,
            Jwt jwt
    );
}
