package com.spshpau.projectservice.repositories;

import com.spshpau.projectservice.model.ProjectMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, UUID> {
}
