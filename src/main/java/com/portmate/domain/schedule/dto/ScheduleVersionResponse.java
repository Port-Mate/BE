package com.portmate.domain.schedule.dto;

import com.portmate.domain.schedule.entity.ScheduleVersion;
import com.portmate.domain.schedule.vo.ScheduleContent;
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
    private String pier;
    private String berth;
    private LocalDate startDt;
    private LocalDate endDt;
    private ScheduleVersion.VersionStatus status;
    private String createdBy;
    private String createdByName;
    private String comment;
    private LocalDateTime createdAt;
    private List<ScheduleContent> scheduleContents;
}