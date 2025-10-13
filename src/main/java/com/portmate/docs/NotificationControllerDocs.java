package com.portmate.docs;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import com.portmate.domain.notification.dto.NotificationPayload;
import com.portmate.domain.notification.dto.NotificationResponse;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.ApiResponse;

@Tag(name = "알림 API", description = "실시간 알림 구독 및 알림 관리 API")
public interface NotificationControllerDocs {

	@Operation(
		summary = "SSE 알림 구독", 
		description = "실시간 알림을 받기 위한 SSE(Server-Sent Events) 연결을 생성합니다. 인증된 사용자만 구독할 수 있습니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "SSE 연결 성공",
			content = @Content(
				mediaType = "text/event-stream"
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
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 에러",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "서버 에러",
					summary = "SSE 연결 생성 실패",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_500",
					  "message": "서버 에러, 관리자에게 문의 바랍니다."
					}
					"""
				)
			)
		)
	})
	SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails);

	@Operation(
		summary = "알림 발송", 
		description = "특정 사용자에게 알림을 발송합니다. Kafka를 통해 비동기로 처리됩니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "알림 발송 요청 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "발송 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": "알림 발행 완료"
					}
					"""
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
		)
	})
	ResponseEntity<ApiResponse<String>> sendNotifications(
		@RequestBody(
			description = "알림 발송 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = NotificationPayload.class),
				examples = @ExampleObject(
					name = "알림 발송 예시",
					value = """
					{
					  "userId": "user123",
					  "title": "접안 일정 변경 알림",
					  "body": "귀하의 선박 접안 일정이 변경되었습니다."
					}
					"""
				)
			)
		)
		NotificationPayload payload
	);

	@Operation(
		summary = "읽지 않은 알림 조회", 
		description = "현재 로그인한 사용자의 읽지 않은 알림 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "읽지 않은 알림 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "조회 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": [
					    {
					      "id": "673a1b2c3d4e5f6g7h8i9j0k",
					      "title": "접안 일정 확정",
					      "body": "선박 S-001의 접안 일정이 확정되었습니다.",
					      "isRead": false,
					      "createdAt": "2025-10-13T14:30:00"
					    },
					    {
					      "id": "673a1b2c3d4e5f6g7h8i9j1k",
					      "title": "선석 배정 완료",
					      "body": "귀하의 선박에 선석이 배정되었습니다.",
					      "isRead": false,
					      "createdAt": "2025-10-13T10:15:00"
					    }
					  ]
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
	ResponseEntity<ApiResponse<List<NotificationResponse>>> unread(
		@AuthenticationPrincipal CustomUserDetails userDetails
	);

	@Operation(
		summary = "알림 읽음 처리", 
		description = "여러 알림을 일괄적으로 읽음 처리합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "읽음 처리 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "처리 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다."
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "유효하지 않은 ID",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_400",
					  "message": "유효하지 않은 알림 ID입니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<Void>> markAsRead(
		@RequestBody(
			description = "읽음 처리할 알림 ID 목록",
			required = true,
			content = @Content(
				examples = @ExampleObject(
					name = "알림 ID 목록 예시",
					value = """
					[
					  "673a1b2c3d4e5f6g7h8i9j0k",
					  "673a1b2c3d4e5f6g7h8i9j1k"
					]
					"""
				)
			)
		)
		List<String> ids
	);

}
