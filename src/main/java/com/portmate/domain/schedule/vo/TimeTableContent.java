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

    // 선박명 (Vessel Name)
    private String vesselName;

    // IMO 번호 또는 호출 부호 (Call Sign)
    private String imoOrCallSign;

    // 항차 (Voyage No.)
    private String voyageNo;

    // ETA (입항 예정)
    private String etaTime;

    // ETD (출항 예정)
    private String etdTime;

    // 도착 항구 (From Port)
    private String fromPort;

    // 다음 항구 (Next Port)
    private String nextPort;

    // 화물 종류 (Cargo Type)
    private String cargoType;

    // 톤수 (Tonnage)
    private String tonnage;

    // 국적 (Flag)
    private String flag;

    // 비고 (Remark)
    private String remark;

    public static TimeTableContent from(ScheduleContent sc) {
        return TimeTableContent.builder()
                .no(sc.getNo())
                .vesselName(sc.getVesselName())
                .imoOrCallSign(sc.getImoOrCallSign())
                .voyageNo(sc.getVoyageNo())
                .etaTime(sc.getEta().substring(12))
                .etdTime(sc.getEtd().substring(12))
                .fromPort(sc.getFromPort())
                .nextPort(sc.getNextPort())
                .cargoType(sc.getCargoType())
                .tonnage(sc.getTonnage())
                .flag(sc.getFlag())
                .remark(sc.getRemark())
                .build();
    }
}
