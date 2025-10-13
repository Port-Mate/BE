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
    
    // 입항/출항 구분
    private String type; // "입항" 또는 "출항"
    
    // 시간
    private String time; // "HH:mm" 형태
    
    // 전체 ETA/ETD
    private String eta;
    private String etd;

    public static TimeTableContent fromArrival(ScheduleContent sc) {
        String etaDate = sc.getEta().substring(0, 10);
        String etaTime = sc.getEta().substring(11, 16); // "HH:mm"
        
        return TimeTableContent.builder()
                .no(sc.getNo())
                .id(sc.getId())
                .vesselName(sc.getVesselName())
                .type("입항")
                .time(etaTime)
                .eta(sc.getEta())
                .etd(sc.getEtd())
                .build();
    }
    
    public static TimeTableContent fromDeparture(ScheduleContent sc) {
        String etdDate = sc.getEtd().substring(0, 10);
        String etdTime = sc.getEtd().substring(11, 16); // "HH:mm"
        
        return TimeTableContent.builder()
                .no(sc.getNo())
                .id(sc.getId())
                .vesselName(sc.getVesselName())
                .type("출항")
                .time(etdTime)
                .eta(sc.getEta())
                .etd(sc.getEtd())
                .build();
    }
}
