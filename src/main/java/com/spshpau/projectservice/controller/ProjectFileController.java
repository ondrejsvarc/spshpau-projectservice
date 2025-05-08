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

    /**
     * Uploads a file to a specified project.
     *
     * @param projectId The ID of the project to which the file will be uploaded.
     * @param file The file to upload.
     * @param description An optional description for the file.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the ProjectFileResponseDto for the uploaded file and HTTP status.
     * @throws IOException If an I/O error occurs during file processing.
     */
    ResponseEntity<ProjectFileResponseDto> uploadProjectFile(@PathVariable UUID projectId,
                                                             @RequestPart("file") MultipartFile file,
                                                             @RequestPart(value = "description", required = false) String description,
                                                             Jwt jwt) throws IOException;

    /**
     * Retrieves a list of files for a specified project.
     * This returns the latest version of each file.
     *
     * @param projectId The ID of the project for which to retrieve files.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing a List of ProjectFileResponseDto and HTTP status.
     */
    ResponseEntity<List<ProjectFileResponseDto>> getProjectFiles(@PathVariable UUID projectId,
                                                                 Jwt jwt);

    /**
     * Retrieves the metadata for a specific file within a project.
     *
     * @param projectId The ID of the project to which the file belongs.
     * @param fileId The ID of the file to retrieve metadata for.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the ProjectFileResponseDto and HTTP status.
     */
    ResponseEntity<ProjectFileResponseDto> getProjectFileMetadata(@PathVariable UUID projectId,
                                                                  @PathVariable UUID fileId,
                                                                  Jwt jwt);

    /**
     * Generates a pre-signed download URL for a specific project file.
     *
     * @param projectId The ID of the project to which the file belongs.
     * @param fileId The ID of the file for which to generate the download URL.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the FileDownloadDto (which includes the URL) and HTTP status.
     */
    ResponseEntity<FileDownloadDto> getProjectFileDownloadUrl(@PathVariable UUID projectId,
                                                              @PathVariable UUID fileId,
                                                              Jwt jwt);

    /**
     * Deletes a specific file from a project.
     *
     * @param projectId The ID of the project from which to delete the file.
     * @param fileId The ID of the file to delete.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> deleteProjectFile(@PathVariable UUID projectId,
                                           @PathVariable UUID fileId,
                                           Jwt jwt);

    /**
     * Retrieves all versions of a specific file by its original filename within a project.
     *
     * @param projectId The ID of the project.
     * @param originalFilename The original filename to search for.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing a List of ProjectFileResponseDto representing all versions of the file, ordered by upload timestamp descending.
     */
    ResponseEntity<List<ProjectFileResponseDto>> getAllVersionsOfFileByName(
            @PathVariable UUID projectId,
            @RequestParam("filename") String originalFilename,
            Jwt jwt
    );
}
