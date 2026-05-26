package com.sahith.devsecops.repository;

import com.sahith.devsecops.entity.CodeQualityReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeQualityReportRepository extends JpaRepository<CodeQualityReport, Long> {

    List<CodeQualityReport> findByProjectId(Long projectId);
}
