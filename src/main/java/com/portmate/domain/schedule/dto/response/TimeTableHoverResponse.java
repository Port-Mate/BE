package com.portmate.domain.schedule.dto.response;

import com.portmate.domain.schedule.vo.ScheduleContent;

public record TimeTableHoverResponse(
        int no,
        String vesselName,
        String imoOrCallSign,
        String voyageNo,
        String etaTime,
        String etdTime,
        String fromPort,
        String nextPort,
        String cargoType,
        String tonnage,
        String flag,
        String remark
) {
    public static TimeTableHoverResponse from(ScheduleContent sc) {
        return new TimeTableHoverResponse(
                sc.getNo(),
                sc.getVesselName(),
                sc.getImoOrCallSign(),
                sc.getVoyageNo(),
                sc.getEta() != null && sc.getEta().length() >= 12 ? sc.getEta().substring(12) : sc.getEta(),
                sc.getEtd() != null && sc.getEtd().length() >= 12 ? sc.getEtd().substring(12) : sc.getEtd(),
                sc.getFromPort(),
                sc.getNextPort(),
                sc.getCargoType(),
                sc.getTonnage(),
                sc.getFlag(),
                sc.getRemark()
        );
    }
}
