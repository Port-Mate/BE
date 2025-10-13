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

import com.portmate.domain.schedule.dto.*;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.ApiResponse;
import com.portmate.global.response.PageResponse;

import java.util.List;

@Tag(name = "스케줄 버전 API", description = "스케줄 변경 요청 및 승인 관리 API")
public interface ScheduleVersionControllerDocs {

	@Operation(
		summary = "스케줄 변경 검증", 
		description = "스케줄 변경 요청 전에 유효성을 검증합니다. 선박 간 충돌, 계류장 크기 등을 검사합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "검증 완료 (유효 여부는 응답 내용 참조)",
			content = @Content(
				mediaType = "application/json",
				examples = {
					@ExampleObject(
						name = "검증 성공",
						value = """
						{
						  "isSuccess": true,
						  "code": "COMMON200",
						  "message": "성공입니다.",
						  "result": {
						    "valid": true,
						    "errors": []
						  }
						}
						"""
					),
					@ExampleObject(
						name = "검증 실패",
						value = """
						{
						  "isSuccess": true,
						  "code": "COMMON200",
						  "message": "성공입니다.",
						  "result": {
						    "valid": false,
						    "errors": [
						      {
						        "shipId": "ship-001",
						        "errorType": "TIME_CONFLICT",
						        "message": "다른 선박과 시간이 겹칩니다."
						      }
						    ]
						  }
						}
						"""
					)
				}
			)
		)
	})
	ResponseEntity<ApiResponse<ScheduleValidateResponse>> validateSchedule(
		@RequestBody(
			description = "검증할 스케줄 배치 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = ScheduleValidateRequest.class),
				examples = @ExampleObject(
					name = "검증 요청 예시",
					value = """
					{
					  "scheduleId": "schedule-2025-10-001",
					  "placements": [
					    {
					      "vesselName": "HARMONY ACE",
					      "pier": "1부두",
					      "berth": "A-1",
					      "eta": "2025-10-13T14:00:00",
					      "etd": "2025-10-13T16:00:00"
					    }
					  ]
					}
					"""
				)
			)
		)
		ScheduleValidateRequest request
	);

	@Operation(
		summary = "스케줄 변경 저장", 
		description = "검증된 스케줄 변경사항을 버전으로 저장합니다. 승인 대기 상태로 생성됩니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "변경사항 저장 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "저장 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "versionId": "version-2025-10-001",
					    "status": "PENDING",
					    "createdAt": "2025-10-13T15:30:00"
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
	ResponseEntity<ApiResponse<ScheduleModifyResponse>> saveScheduleModification(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody(
			description = "저장할 스케줄 변경 정보",
			required = true,
			content = @Content(
				schema = @Schema(implementation = ScheduleModifyRequest.class),
				examples = @ExampleObject(
					name = "저장 요청 예시",
					value = """
					{
					  "scheduleId": "schedule-2025-10-001",
					  "placements": [
					    {
					      "vesselName": "HARMONY ACE",
					      "pier": "1부두",
					      "berth": "A-2",
					      "eta": "2025-10-13T15:00:00",
					      "etd": "2025-10-13T17:00:00"
					    }
					  ],
					  "comment": "화주 요청에 따른 계류장 변경"
					}
					"""
				)
			)
		)
		ScheduleModifyRequest request
	);

	@Operation(
		summary = "스케줄 버전 상세 조회", 
		description = "특정 버전의 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "버전 조회 성공",
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
					    "versionId": "version-2025-10-001",
					    "originalScheduleId": "schedule-2025-10-001",
					    "pier": "1부두",
					    "berth": "A-1",
					    "startDt": "2025-10-13",
					    "endDt": "2025-10-20",
					    "status": "PENDING",
					    "createdBy": "user123",
					    "createdByName": "홍길동",
					    "comment": "화주 요청에 따른 계류장 변경",
					    "createdAt": "2025-10-13T15:30:00",
					    "scheduleContents": [],
					    "changedContents": [],
					    "reviewers": [
					      {
					        "companyName": "부산항만공사",
					        "status": "PENDING"
					      }
					    ]
					  }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "버전 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "버전 없음",
					value = """
					{
					  "isSuccess": false,
					  "code": "VERSION_404",
					  "message": "해당 버전을 찾을 수 없습니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<ScheduleVersionResponse>> getScheduleVersion(
		@Parameter(description = "버전 ID", required = true)
		@PathVariable String versionId
	);

	@Operation(
		summary = "전체 버전 목록 조회", 
		description = "모든 스케줄 버전 목록을 페이징하여 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "목록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "목록 조회 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "content": [],
					    "currentPage": 0,
					    "totalPages": 5,
					    "totalElements": 50,
					    "size": 10
					  }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<PageResponse<List<ScheduleVersionResponse>>>> getAllVersions(
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기", example = "10")
		@RequestParam(defaultValue = "10") int size
	);

	@Operation(
		summary = "버전 승인", 
		description = "스케줄 변경 버전을 승인합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "승인 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "승인 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "versionId": "version-2025-10-001",
					    "companyName": "부산항만공사",
					    "reviewerStatus": "APPROVED",
					    "overallStatus": "APPROVED",
					    "actionTime": "2025-10-13T16:00:00",
					    "message": "승인되었습니다."
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
	ResponseEntity<ApiResponse<ReviewActionResponse>> approveVersion(
		@Parameter(description = "버전 ID", required = true)
		@PathVariable String versionId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody(
			description = "승인 코멘트",
			required = true,
			content = @Content(
				schema = @Schema(implementation = ReviewActionRequest.class),
				examples = @ExampleObject(
					name = "승인 요청 예시",
					value = """
					{
					  "comment": "변경사항을 확인했으며 승인합니다."
					}
					"""
				)
			)
		)
		ReviewActionRequest request
	);

	@Operation(
		summary = "버전 반려", 
		description = "스케줄 변경 버전을 반려합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "반려 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "반려 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "versionId": "version-2025-10-001",
					    "companyName": "부산항만공사",
					    "reviewerStatus": "REJECTED",
					    "overallStatus": "REJECTED",
					    "actionTime": "2025-10-13T16:00:00",
					    "message": "반려되었습니다."
					  }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<ReviewActionResponse>> rejectVersion(
		@Parameter(description = "버전 ID", required = true)
		@PathVariable String versionId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody(
			description = "반려 사유",
			required = true,
			content = @Content(
				schema = @Schema(implementation = ReviewActionRequest.class),
				examples = @ExampleObject(
					name = "반려 요청 예시",
					value = """
					{
					  "comment": "계류장 크기가 선박 톤수에 맞지 않습니다. 재검토 부탁드립니다."
					}
					"""
				)
			)
		)
		ReviewActionRequest request
	);

	@Operation(
		summary = "버전 적용", 
		description = "승인된 버전을 실제 스케줄에 적용합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "적용 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "적용 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "versionId": "version-2025-10-001",
					    "originalScheduleId": "schedule-2025-10-001",
					    "status": "APPLIED",
					    "appliedAt": "2025-10-13T16:30:00",
					    "message": "스케줄에 성공적으로 적용되었습니다."
					  }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "적용 불가능한 상태",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "적용 실패",
					value = """
					{
					  "isSuccess": false,
					  "code": "VERSION_400",
					  "message": "승인되지 않은 버전은 적용할 수 없습니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<ScheduleApplyResponse>> applyVersionToSchedule(
		@Parameter(description = "버전 ID", required = true)
		@PathVariable String versionId
	);

}

