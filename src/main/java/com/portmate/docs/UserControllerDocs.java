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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.portmate.domain.user.dto.RegisterRequest;
import com.portmate.domain.user.dto.UserInfoResponse;
import com.portmate.domain.user.dto.UserStatusRequest;
import com.portmate.domain.user.dto.UserStatusResponse;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.ApiResponse;
import com.portmate.global.response.PageResponse;

import jakarta.validation.constraints.Min;
import java.util.List;

@Tag(name = "사용자 API", description = "사용자 정보 조회 및 회원가입, 승인 관리 API")
public interface UserControllerDocs {

	@Operation(
		summary = "내 정보 조회", 
		description = "현재 로그인한 사용자의 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "사용자 정보 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "조회 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "name": "홍길동",
					    "email": "hong@example.com"
					  }
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
	ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	);

	@Operation(
		summary = "승인 대기 사용자 목록 조회", 
		description = "회원가입 후 승인 대기 중인 사용자 목록을 페이징하여 조회합니다. 관리자용 API입니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "조회 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "content": [
					      {
					        "userId": "user123",
					        "userName": "김철수",
					        "userCompany": "한진해운",
					        "userStatus": "PENDING",
					        "userRole": "AGENT"
					      }
					    ],
					    "currentPage": 1,
					    "totalPages": 3,
					    "totalElements": 25,
					    "size": 10
					  }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<PageResponse<List<UserStatusResponse>>>> getPendingUsers(
		@Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
		@RequestParam(defaultValue = "1") @Min(value = 1) int page,
		@Parameter(description = "페이지 크기", example = "10")
		@RequestParam(defaultValue = "10") @Min(value = 1) int size
	);

	@Operation(
		summary = "회원가입", 
		description = "새로운 사용자를 등록합니다. 등록 후 관리자 승인이 필요합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "회원가입 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "가입 성공",
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
					name = "유효성 검사 실패",
					summary = "필수 필드 누락 또는 형식 오류",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_400",
					  "message": "이메일 형식이 올바르지 않습니다."
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "409",
			description = "중복된 이메일",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "이메일 중복",
					value = """
					{
					  "isSuccess": false,
					  "code": "USER_409",
					  "message": "이미 등록된 이메일입니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<Void>> postRegister(
		@RequestBody(
			description = "회원가입 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = RegisterRequest.class),
				examples = @ExampleObject(
					name = "회원가입 요청 예시",
					value = """
					{
					  "name": "김철수",
					  "email": "kim@example.com",
					  "password": "securePassword123!",
					  "company": "한진해운",
					  "phone": "010-1234-5678",
					  "userRole": "AGENT"
					}
					"""
				)
			)
		)
		RegisterRequest registerRequest
	);

	@Operation(
		summary = "사용자 상태 변경", 
		description = "사용자의 승인 상태를 변경합니다. 관리자용 API입니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "상태 변경 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "변경 성공",
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
			responseCode = "404",
			description = "사용자 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "사용자 없음",
					value = """
					{
					  "isSuccess": false,
					  "code": "USER_404",
					  "message": "해당 사용자를 찾을 수 없습니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<Void>> putUserStatus(
		@Parameter(description = "사용자 ID", required = true)
		@PathVariable("userId") String userId,
		@RequestBody(
			description = "변경할 사용자 상태 (PENDING: 대기, ACTIVE: 승인, EXIT: 탈퇴)",
			required = true,
			content = @Content(
				schema = @Schema(implementation = UserStatusRequest.class),
				examples = @ExampleObject(
					name = "상태 변경 예시",
					value = """
					{
					  "userStatus": "ACTIVE"
					}
					"""
				)
			)
		)
		UserStatusRequest request
	);

}

