package com.portmate.domain.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.portmate.docs.NotificationControllerDocs;
import com.portmate.domain.notification.domain.Notification;
import com.portmate.domain.notification.dto.NotificationPayload;
import com.portmate.domain.notification.dto.NotificationResponse;
import com.portmate.domain.notification.service.NotificationService;
import com.portmate.domain.notification.service.NotificationSseService;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.kafka.KafkaProducer;
import com.portmate.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController implements NotificationControllerDocs {

	private final KafkaProducer kafkaProducer;
	private final NotificationSseService sseService;
	private final NotificationService notificationService;

	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return sseService.subscribe(userDetails.getUserId());
	}

	@PostMapping("/send")
	public ResponseEntity<ApiResponse<String>> sendNotifications(@RequestBody @Valid NotificationPayload payload) {
		kafkaProducer.send("notification-topic",payload);
		return ResponseEntity.ok(ApiResponse.onSuccess("알림 발행 완료"));
	}

	@GetMapping("/unread")
	public ResponseEntity<ApiResponse<List<NotificationResponse>>> unread(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.getUnreadNotifications(userDetails)));
	}

	@PostMapping("/read")
	public ResponseEntity<ApiResponse<Void>> markAsRead(@RequestBody List<String> ids) {
		notificationService.markAsReadBatch(ids);
		return ResponseEntity.ok(ApiResponse.onSuccessVoid());
	}
}
