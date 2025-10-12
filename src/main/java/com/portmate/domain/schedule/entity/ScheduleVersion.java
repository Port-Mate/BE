package com.portmate.domain.schedule.entity;

import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.domain.schedule.vo.ScheduleVersionContent;
import com.portmate.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    
    private List<ScheduleContent> scheduleContents; // 전체 스케줄 내용 (변경 후)
    
    private List<ScheduleVersionContent> changedContents; // 변경된 선박들의 변경 정보
    
    private VersionStatus status;
    
    private String createdBy; // 수정자 ID
    
    private String createdByName; // 수정자 이름
    
    private String comment; // 수정 사유

    private Map<String, VersionReviewerStatus> reviewers;

    public enum VersionReviewerStatus {
        PENDING,   // 승인 대기
        APPROVED,  // 승인됨
        REJECTED,  // 거부됨
    }
    
    public enum VersionStatus {
        PENDING,   // 승인 대기
        APPROVED,  // 모든 리뷰어 승인 완료
        REJECTED,  // 거부됨
        APPLIED,   // 스케줄에 반영 완료
        CANCELLED  // 취소됨
    }
    
    // 상태 업데이트를 위한 단순한 setter 메서드들
    public void updateStatus(VersionStatus newStatus) {
        this.status = newStatus;
    }
    
    public void updateReviewerStatus(String companyName, VersionReviewerStatus reviewerStatus) {
        if (reviewers != null && reviewers.containsKey(companyName)) {
            reviewers.put(companyName, reviewerStatus);
        }
    }
}
