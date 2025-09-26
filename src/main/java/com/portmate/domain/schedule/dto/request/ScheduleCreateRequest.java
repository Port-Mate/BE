package com.portmate.domain.schedule.dto.request;

import java.time.LocalDate;

public record ScheduleCreateRequest(
        String portName, LocalDate startDt, LocalDate endDt ) {
}
