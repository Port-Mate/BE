package com.portmate.domain.schedule.entity;

import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "schedule_versions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVersion extends BaseEntity {
    @Id
    private String versionId;
    
    private String originalScheduleId; // 원본 스케줄 참조
    
    private String pier;
    
    private String berth;
    
    private LocalDate startDt;
    
    private LocalDate endDt;
    
    private List<ScheduleContent> scheduleContents; // 변경된 선박 배치
    
    private VersionStatus status;
    
    private String createdBy; // 수정자 ID
    
    private String createdByName; // 수정자 이름
    
    private String comment; // 수정 사유
    
    public enum VersionStatus {
        PENDING,   // 승인 대기
        APPROVED,  // 승인됨
        REJECTED,  // 거부됨
        CANCELLED  // 취소됨
    }
    
    public void approve() {
        this.status = VersionStatus.APPROVED;
    }
    
    public void reject() {
        this.status = VersionStatus.REJECTED;
    }
    
    public void cancel() {
        this.status = VersionStatus.CANCELLED;
    }
}
