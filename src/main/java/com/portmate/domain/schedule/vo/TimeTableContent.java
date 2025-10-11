package com.portmate.domain.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableContent {
    // 순번 (Row 번호)
    private int no;

    private String id;

    // 선박명 (Vessel Name)
    private String vesselName;

    public static TimeTableContent from(ScheduleContent sc) {
        return TimeTableContent.builder()
                .no(sc.getNo())
                .id(sc.getId())
                .vesselName(sc.getVesselName())
                .build();
    }
}
