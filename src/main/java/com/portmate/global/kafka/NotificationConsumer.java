package com.portmate.global.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portmate.domain.notification.dto.NotificationPayload;
import com.portmate.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
	private final NotificationService notificationService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "notification-topic", groupId = "notification-group")
	public void consumeNotification(String record) {
		try {
			NotificationPayload payload = objectMapper.readValue(record, NotificationPayload.class);
			notificationService.handle(payload);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
