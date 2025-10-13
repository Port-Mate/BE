package com.portmate.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.portmate.domain.notification.dto.FcmTokenRequest;
import com.portmate.global.auth.CustomUserDetails;

@Tag(name = "FCM 토큰 API", description = "Firebase Cloud Messaging 토큰 관리 API")
public interface FcmTokenControllerDocs {

	@Operation(
		summary = "FCM 토큰 등록", 
		description = "사용자의 디바이스 FCM 토큰을 등록하여 푸시 알림을 받을 수 있도록 합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "토큰 등록 성공",
			content = @Content(
				mediaType = "text/plain",
				examples = @ExampleObject(
					name = "등록 성공",
					value = "토큰 저장 완료"
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "유효성 검사 실패",
					summary = "필수 필드 누락 또는 형식 오류",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_400",
					  "message": "요청 파라미터가 올바르지 않습니다."
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "인증 실패",
					summary = "로그인이 필요합니다",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_401",
					  "message": "인증이 필요합니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<String> registerToken(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody(
			description = "FCM 토큰 등록 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = FcmTokenRequest.class),
				examples = @ExampleObject(
					name = "FCM 토큰 등록 예시",
					value = """
					{
					  "token": "fJ8K9mN2pQ5rS6tU7vW8xY9zA0bC1dE2fG3hI4jK5lM6nO7pQ8rS9tU0",
					  "platform": "ANDROID"
					}
					"""
				)
			)
		)
		FcmTokenRequest req
	);

}

