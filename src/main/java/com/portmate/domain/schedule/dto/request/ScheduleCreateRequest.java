package com.portmate.domain.schedule.dto.request;

import java.time.LocalDate;

public record ScheduleCreateRequest(
        String port, LocalDate startDt, LocalDate endDt ) {
}
