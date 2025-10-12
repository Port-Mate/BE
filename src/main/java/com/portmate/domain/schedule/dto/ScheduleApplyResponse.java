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
public class ScheduleApplyResponse {
    private String versionId;
    private String originalScheduleId;
    private ScheduleVersion.VersionStatus status;
    private LocalDateTime appliedAt;
    private String message;
}
