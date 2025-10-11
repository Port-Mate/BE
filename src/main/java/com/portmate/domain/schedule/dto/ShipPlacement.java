package com.portmate.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipPlacement {
    private String vesselName;
    private String pier;
    private String berth;
    private String eta;
    private String etd;
}
