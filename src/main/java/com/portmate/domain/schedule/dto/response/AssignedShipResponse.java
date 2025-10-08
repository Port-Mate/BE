package com.portmate.domain.schedule.dto.response;

import java.time.LocalDateTime;

public record AssignedShipResponse(
	String vesselName,
	String cargoType,
	String pierName,
	String berthName,
	LocalDateTime eta,
	LocalDateTime etd

) {
}
