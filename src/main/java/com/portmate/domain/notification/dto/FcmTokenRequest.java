package com.portmate.domain.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenRequest(
	@NotBlank
	String token,
	@NotBlank String platform
) {
}
