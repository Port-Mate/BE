package com.portmate.domain.schedule.dto.response;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.TimeTableWrapper;

import java.time.LocalDate;
import java.util.List;

public record ScheduleListResponse(
        String scheduleId,
        String pierName,
        String berthName,
        LocalDate startDt,
        LocalDate endDt,
        List<TimeTableWrapper> timeTableWrappers
) {
    public static ScheduleListResponse from(Schedule schedule, List<TimeTableWrapper> items) {
        return new ScheduleListResponse(
                schedule.getScheduleId(), schedule.getPier(),
                schedule.getBerth(), schedule.getStartDt(),
                schedule.getEndDt(), items
        );
    }
}
