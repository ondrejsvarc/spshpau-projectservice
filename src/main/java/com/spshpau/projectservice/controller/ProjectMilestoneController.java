package com.spshpau.projectservice.controller;

import com.spshpau.projectservice.dto.MilestoneCreateDto;
import com.spshpau.projectservice.dto.MilestoneResponseDto;
import com.spshpau.projectservice.dto.MilestoneUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface ProjectMilestoneController {

    ResponseEntity<MilestoneResponseDto> createMilestone(@PathVariable UUID projectId,
                                                         @Valid @RequestBody MilestoneCreateDto milestoneDto,
                                                         Jwt jwt);

    ResponseEntity<MilestoneResponseDto> getMilestoneById(@PathVariable UUID projectId,
                                                          @PathVariable UUID milestoneId,
                                                          Jwt jwt);

    ResponseEntity<Page<MilestoneResponseDto>> getMilestonesForProject(@PathVariable UUID projectId,
                                                                       Jwt jwt,
                                                                       Pageable pageable);

    ResponseEntity<MilestoneResponseDto> updateMilestone(@PathVariable UUID projectId,
                                                         @PathVariable UUID milestoneId,
                                                         @Valid @RequestBody MilestoneUpdateDto milestoneDto,
                                                         Jwt jwt);

    ResponseEntity<Void> deleteMilestone(@PathVariable UUID projectId,
                                         @PathVariable UUID milestoneId,
                                         Jwt jwt);
}