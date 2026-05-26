package com.sahith.devsecops.repository;

import com.sahith.devsecops.entity.Approval;
import com.sahith.devsecops.entity.ApprovalStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByDeploymentId(Long deploymentId);

    Optional<Approval> findFirstByDeploymentIdAndApprovalStatus(Long deploymentId, ApprovalStatus approvalStatus);
}
