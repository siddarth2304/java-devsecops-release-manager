package com.sahith.devsecops.repository;

import com.sahith.devsecops.entity.Deployment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    List<Deployment> findByBuildId(Long buildId);
}
