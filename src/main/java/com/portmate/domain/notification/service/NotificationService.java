package com.portmate.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portmate.domain.notification.domain.Notification;
import com.portmate.domain.notification.dto.NotificationPayload;
import com.portmate.domain.notification.dto.NotificationResponse;
import com.portmate.domain.notification.repository.NotificationRepository;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

import io.github.kamillcream.mpa.TransactionalMongo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final NotificationSseService notificationSseService;
	private final FcmService fcmService;

	public void handle(NotificationPayload payload) {
		String userId = payload.getUserId();
		String title = payload.getTitle();
		String body = payload.getBody();

		Notification noti = Notification.builder()
			.userId(userId)
			.title(title)
			.body(body)
			.isRead(false)
			.build();
		notificationRepository.save(noti);

		notificationSseService.sendToClient(userId, noti);

		fcmService.sendNotificationToUser(userId, title, body);

	}
	public List<NotificationResponse> getUnreadNotifications(CustomUserDetails userDetails)
	{
		List<NotificationResponse> notifications = notificationRepository.findByUserIdAndIsReadFalse(userDetails.getUserId())
			.stream()
			.map(NotificationResponse::from)
			.toList();

		return notifications;
	}

	@TransactionalMongo
	public void markAsRead(String notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(()-> new GlobalException(ErrorStatus.NOTIFICATION_NOT_FOUND));
		notification.setRead(true);

	}

	@TransactionalMongo
	public void markAsReadBatch(List<String> ids) {
		List<Notification> notifications = notificationRepository.findAllByIds(ids);

		if (notifications.isEmpty()) {
			throw new GlobalException(ErrorStatus.NOTIFICATION_NOT_FOUND);
		}

		notifications.forEach(noti -> noti.setRead(true));
		notificationRepository.saveAll(notifications);
	}
}
