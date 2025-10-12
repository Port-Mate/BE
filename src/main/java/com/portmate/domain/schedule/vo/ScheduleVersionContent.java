package com.portmate.domain.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVersionContent {
    
    private String vesselName; // 선박명
    private ChangeType changeType; // 변경 타입
    private ScheduleContent originalContent; // 변경 전 정보
    private ScheduleContent modifiedContent; // 변경 후 정보
    
    public enum ChangeType {
        ETA_CHANGED,    // ETA 변경
        ETD_CHANGED,    // ETD 변경
        PIER_CHANGED,   // 부두 변경
        BERTH_CHANGED,  // 선석 변경
        MULTIPLE_CHANGED // 여러 항목 변경
    }

    public String getChangeDescription() {
        if (originalContent == null || modifiedContent == null) {
            return "정보 없음";
        }
        
        StringBuilder description = new StringBuilder();

        if (!originalContent.getEta().equals(modifiedContent.getEta())) {
            description.append(String.format("ETA: %s → %s, ", 
                originalContent.getEta(), modifiedContent.getEta()));
        }

        if (!originalContent.getEtd().equals(modifiedContent.getEtd())) {
            description.append(String.format("ETD: %s → %s, ", 
                originalContent.getEtd(), modifiedContent.getEtd()));
        }

        if (!originalContent.getPier().equals(modifiedContent.getPier())) {
            description.append(String.format("부두: %s → %s, ", 
                originalContent.getPier(), modifiedContent.getPier()));
        }

        if (!originalContent.getBerth().equals(modifiedContent.getBerth())) {
            description.append(String.format("선석: %s → %s, ", 
                originalContent.getBerth(), modifiedContent.getBerth()));
        }

        if (description.length() > 0) {
            description.setLength(description.length() - 2);
        }
        
        return description.toString();
    }
}
