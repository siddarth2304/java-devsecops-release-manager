package com.sahith.devsecops.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_quality_reports")
public class CodeQualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private int bugsCount;

    @Column(nullable = false)
    private int vulnerabilitiesCount;

    @Column(nullable = false)
    private int codeSmellsCount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal coveragePercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualityGateStatus qualityGateStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime scannedAt;

    @PrePersist
    public void prePersist() {
        this.scannedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getBugsCount() {
        return bugsCount;
    }

    public void setBugsCount(int bugsCount) {
        this.bugsCount = bugsCount;
    }

    public int getVulnerabilitiesCount() {
        return vulnerabilitiesCount;
    }

    public void setVulnerabilitiesCount(int vulnerabilitiesCount) {
        this.vulnerabilitiesCount = vulnerabilitiesCount;
    }

    public int getCodeSmellsCount() {
        return codeSmellsCount;
    }

    public void setCodeSmellsCount(int codeSmellsCount) {
        this.codeSmellsCount = codeSmellsCount;
    }

    public BigDecimal getCoveragePercentage() {
        return coveragePercentage;
    }

    public void setCoveragePercentage(BigDecimal coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public QualityGateStatus getQualityGateStatus() {
        return qualityGateStatus;
    }

    public void setQualityGateStatus(QualityGateStatus qualityGateStatus) {
        this.qualityGateStatus = qualityGateStatus;
    }

    public LocalDateTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }
}
