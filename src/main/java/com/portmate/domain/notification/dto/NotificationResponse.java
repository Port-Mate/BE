package com.portmate.domain.notification.dto;


import com.portmate.domain.notification.domain.Notification;
import lombok.Builder;

@Builder
public record NotificationResponse(
	String id,
	String title,
	String body,
	boolean isRead,
	String createdAt
) {
	public static NotificationResponse from(Notification noti) {
		return NotificationResponse.builder()
			.id(noti.getId())
			.title(noti.getTitle())
			.body(noti.getBody())
			.isRead(noti.isRead())
			.createdAt(noti.getCreatedAt().toString()) // 필요시 포맷팅
			.build();
	}
}

