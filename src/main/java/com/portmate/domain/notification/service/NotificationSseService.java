package com.portmate.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.portmate.domain.notification.domain.Notification;

@Service
public class NotificationSseService {
	private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

	public SseEmitter subscribe(String userId) {
		SseEmitter emitter = new SseEmitter(60 * 1000L * 10);
		emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

		emitter.onCompletion(() -> removeEmitter(userId, emitter));
		emitter.onTimeout(() -> removeEmitter(userId, emitter));

		try {
			emitter.send(SseEmitter.event().name("INIT").data("connected"));
		} catch (IOException e) {
			removeEmitter(userId, emitter);
		}

		return emitter;
	}
	public void sendToClient(String userId, Notification notification) {
		List<SseEmitter> userEmitters = emitters.get(userId);
		if (userEmitters != null) {
			for (SseEmitter emitter : userEmitters) {
				try {
					emitter.send(SseEmitter.event()
						.name("notification")
						.data(notification));
				} catch (IOException e) {
					removeEmitter(userId, emitter);
				}
			}
		}
	}
	@Scheduled(fixedRate = 30000)
	public void sendPing() {
		emitters.forEach((userId, userEmitters) -> {
			for (SseEmitter emitter : userEmitters) {
				try {
					emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
				} catch (IOException e) {
					removeEmitter(userId, emitter);
				}
			}
		});
	}

	private void removeEmitter(String userId, SseEmitter emitter) {
		List<SseEmitter> userEmitters = emitters.get(userId);
		if (userEmitters != null) {
			userEmitters.remove(emitter);
			if (userEmitters.isEmpty()) {
				emitters.remove(userId);
			}
		}
	}
}

