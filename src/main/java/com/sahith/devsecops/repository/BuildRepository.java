package com.sahith.devsecops.repository;

import com.sahith.devsecops.entity.Build;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildRepository extends JpaRepository<Build, Long> {

    List<Build> findByProjectId(Long projectId);
}
