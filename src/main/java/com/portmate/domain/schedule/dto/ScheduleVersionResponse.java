package com.portmate.domain.schedule.dto;

import com.portmate.domain.schedule.entity.ScheduleVersion;
import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.domain.schedule.vo.ScheduleVersionContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVersionResponse {
    private String versionId;
    private String originalScheduleId;
    private String port;
    private LocalDate startDt;
    private LocalDate endDt;
    private ScheduleVersion.VersionStatus status;
    private String createdBy;
    private String createdByName;
    private String comment;
    private LocalDateTime createdAt;
    private List<ScheduleContent> scheduleContents; // 전체 스케줄 내용
    private List<ScheduleVersionContent> changedContents; // 변경된 선박 정보만
    private List<ReviewerInfo> reviewers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewerInfo {
        private String companyName;
        private ScheduleVersion.VersionReviewerStatus status;
    }
}