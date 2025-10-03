package com.portmate.domain.notification.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayload {
	@NotBlank
	private String userId;
	@NotBlank
	private String title;
	@NotBlank
	private String body;
}
