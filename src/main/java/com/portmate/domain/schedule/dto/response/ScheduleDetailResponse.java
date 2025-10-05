package com.portmate.domain.schedule.dto.response;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.ScheduleContent;

import java.time.LocalDate;
import java.util.List;

public record ScheduleDetailResponse(
        String scheduleId,
        String portName,
        LocalDate startDt,
        LocalDate endDt,
        List<ScheduleContent> scheduleContents
) {
    public static ScheduleDetailResponse from(Schedule schedule) {
        return new ScheduleDetailResponse(
                schedule.getScheduleId(), schedule.getPortName(),
                schedule.getStartDt(), schedule.getEndDt(),
                schedule.getScheduleContents()
        );
    }
}
