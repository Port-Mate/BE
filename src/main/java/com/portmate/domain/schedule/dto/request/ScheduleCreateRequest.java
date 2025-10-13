package com.portmate.domain.schedule.dto.request;

import java.time.LocalDate;

public record ScheduleCreateRequest(
        String pier, LocalDate startDt, LocalDate endDt ) {
}
