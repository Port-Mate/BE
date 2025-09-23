package com.portmate.domain.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portmate.domain.notification.dto.FcmTokenRequest;
import com.portmate.domain.notification.service.FcmService;
import com.portmate.domain.notification.service.UserFcmTokenService;
import com.portmate.global.auth.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmTokenController {


	private final UserFcmTokenService tokenService;
	private final FcmService fcmService;

	@PostMapping("/token")
	public ResponseEntity<String> registerToken(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Valid FcmTokenRequest req) {
		tokenService.saveToken(customUserDetails.getUserId(), req);
		return ResponseEntity.ok("토큰 저장 완료");
	}

	@PostMapping("/send")
	public ResponseEntity<String> send(@RequestParam String userId,
		@RequestParam String title,
		@RequestParam String body) {
		fcmService.sendNotificationToUser(userId, title, body);
		return ResponseEntity.ok("메시지 전송 완료");
	}
}

