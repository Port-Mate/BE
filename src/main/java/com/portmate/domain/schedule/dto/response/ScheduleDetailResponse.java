package com.portmate.domain.schedule.dto.response;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.TimeTableWrapper;

import java.time.LocalDate;
import java.util.List;

public record ScheduleDetailResponse(
        String scheduleId,
        String portName,
        LocalDate startDt,
        LocalDate endDt,
        List<TimeTableWrapper> timeTableWrappers
) {
    public static ScheduleDetailResponse from(Schedule schedule, List<TimeTableWrapper> items) {
        return new ScheduleDetailResponse(
                schedule.getScheduleId(), schedule.getPortName(),
                schedule.getStartDt(), schedule.getEndDt(),
                items
        );
    }
}
