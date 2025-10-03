package com.portmate.domain.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portmate.domain.notification.domain.Notification;
import com.portmate.domain.notification.repository.custom.CustomNotificationRepository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, CustomNotificationRepository {
}
