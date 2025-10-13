package com.portmate.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModifyRequest {
    private String scheduleId;
    private List<ShipPlacement> placements;
    private String comment;
}
