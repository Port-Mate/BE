package com.portmate.domain.schedule.vo;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.csv.CSVRecord;

import java.util.UUID;

/**
 * 선박 일정 엑셀의 한 행(row)을 표현하는 클래스
 */
@Data
@Builder
public class ScheduleContent {

    private String id;

    // 순번 (Row 번호)
    private int no;

    // 선박명 (Vessel Name)
    private String vesselName;

    // IMO 번호 또는 호출 부호 (Call Sign)
    private String imoOrCallSign;

    // 항차 (Voyage No.)
    private String voyageNo;

    // ETA (입항 예정)
    private String eta;

    // ETD (출항 예정)
    private String etd;

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

    private String agent;

    private String pier;
    private String berth;



    public static ScheduleContent from(CSVRecord record) {
        return ScheduleContent.builder()
                .id(UUID.randomUUID().toString())
                .no(Integer.parseInt(record.get("No.")))
                .vesselName(record.get("선박명 (Vessel Name)"))
                .imoOrCallSign(record.get("IMO / Call Sign"))
                .voyageNo(record.get("항차 (Voyage No.)"))
                .eta(record.get("ETA (입항 예정)"))
                .etd(record.get("ETD (출항 예정)"))
                .fromPort(record.get("도착 항구 (From Port)"))
                .nextPort(record.get("다음 항구 (Next Port)"))
                .cargoType(record.get("화물 종류 (Cargo Type)"))
                .tonnage(record.get("톤수 (Tonnage)"))
                .flag(record.get("국적 (Flag)"))
                .remark(record.get("비고 (Remark)"))
                .agent(record.get("대리점"))
                .build();
    }
}