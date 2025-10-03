package com.portmate.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portmate.domain.notification.dto.NotificationPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void send(String topic, NotificationPayload payload) {
		try {
			kafkaTemplate.send(topic, payload.getUserId(),objectMapper.writeValueAsString(payload));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
