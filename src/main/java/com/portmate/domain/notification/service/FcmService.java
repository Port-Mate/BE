package com.portmate.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.portmate.domain.notification.domain.UserFcmToken;
import com.portmate.domain.notification.repository.UserFcmTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

	private final FirebaseMessaging firebaseMessaging;
	private final UserFcmTokenRepository tokenRepository;

	public void sendNotificationToUser(String userId, String title, String body) {
		List<UserFcmToken> tokens = tokenRepository.findByUserId(userId);
		if (tokens.isEmpty()) {
			log.warn("No FCM tokens found for userId={}", userId);
			return;
		}

		for (UserFcmToken tokenEntity : tokens) {
			String token = tokenEntity.getToken();
			Message message = Message.builder()
				.setToken(token)
				.setNotification(Notification.builder()
					.setTitle(title)
					.setBody(body)
					.build())
				.build();

			try {
				String response = firebaseMessaging.send(message);
				log.info("Sent FCM message to userId={}, token={}, response={}", userId, token, response);
			} catch (FirebaseMessagingException e) {
				log.error("Failed to send FCM message to userId={}, token={}, reason={}",
					userId, token, e.getMessage());

				tokenRepository.deleteByUserIdAndToken(userId, token);
			}
		}
	}


	public void sendNotification(String token, String title, String body) {
		Notification notification = Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build();

		Message message = Message.builder()
			.setToken(token)
			.setNotification(notification)
			.build();

		try {
			String response = firebaseMessaging.send(message);
			log.info("Successfully sent message: {}", response);
		} catch (Exception e) {
			log.error("Failed to send message: {}", e.getMessage());
		}
	}
}
