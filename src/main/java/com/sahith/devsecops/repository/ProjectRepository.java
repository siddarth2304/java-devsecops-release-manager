package com.sahith.devsecops.repository;

import com.sahith.devsecops.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
