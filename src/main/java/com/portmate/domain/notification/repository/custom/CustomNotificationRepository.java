package com.portmate.domain.notification.repository.custom;

import java.util.List;
import java.util.Optional;

import com.portmate.domain.notification.domain.Notification;

public interface CustomNotificationRepository {
	List<Notification> findByUserIdAndIsReadFalse(String userId);
	Optional<Notification> findByNotificationsId(String notificationsId);
	List<Notification> findAllByIds(List<String> ids);
}
