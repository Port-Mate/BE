package com.portmate.domain.schedule.dto.request;

import java.time.LocalDate;

public record ScheduleCreateRequest(
        String pier, String berth, LocalDate startDt, LocalDate endDt ) {
}
