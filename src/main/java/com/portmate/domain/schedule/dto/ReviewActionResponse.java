package com.portmate.domain.schedule.dto;

import com.portmate.domain.schedule.entity.ScheduleVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewActionResponse {
    private String versionId;
    private String companyName;
    private ScheduleVersion.VersionReviewerStatus reviewerStatus;
    private ScheduleVersion.VersionStatus overallStatus;
    private LocalDateTime actionTime;
    private String message;
}
