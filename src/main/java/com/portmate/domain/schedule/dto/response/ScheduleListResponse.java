package com.portmate.domain.schedule.dto.response;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.TimeTableWrapper;

import java.time.LocalDate;
import java.util.List;

public record ScheduleListResponse(
        String scheduleId,
        String portName,
        String pierName,
        String berthName,
        LocalDate startDt,
        LocalDate endDt,
        List<TimeTableWrapper> timeTableWrappers
) {
    public static ScheduleListResponse from(Schedule schedule, List<TimeTableWrapper> items) {
        String pier = null;
        String berth = null;
        if (schedule.getScheduleContents() != null && !schedule.getScheduleContents().isEmpty()) {
            pier = schedule.getScheduleContents().get(0).getPier();
            berth = schedule.getScheduleContents().get(0).getBerth();
        }

        return new ScheduleListResponse(
            schedule.getScheduleId(),
            schedule.getPort(),
            pier,
            berth,
            schedule.getStartDt(),
            schedule.getEndDt(),
            items
        );
    }
}
