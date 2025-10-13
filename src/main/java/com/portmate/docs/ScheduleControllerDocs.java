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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.AssignedShipResponse;
import com.portmate.domain.schedule.dto.response.ScheduleListResponse;
import com.portmate.domain.schedule.dto.response.TimeTableHoverResponse;
import com.portmate.global.response.ApiResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "스케줄 API", description = "선박 접안 스케줄 관리 API")
public interface ScheduleControllerDocs {

	@Operation(
		summary = "스케줄 상세 조회", 
		description = "contentId로 특정 선박의 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "스케줄 상세 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "상세 조회 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": {
					    "no": 1,
					    "vesselName": "HARMONY ACE",
					    "imoOrCallSign": "IMO1234567",
					    "voyageNo": "V2025-001",
					    "etaTime": "14:00",
					    "etdTime": "16:00",
					    "fromPort": "부산",
					    "nextPort": "인천",
					    "cargoType": "컨테이너",
					    "tonnage": "50000",
					    "flag": "KR",
					    "remark": "정상 입항"
					  }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "스케줄 정보 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "스케줄 없음",
					value = """
					{
					  "isSuccess": false,
					  "code": "SCHEDULE_404",
					  "message": "해당 스케줄을 찾을 수 없습니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<TimeTableHoverResponse>> getByScheduleId(
		@Parameter(description = "스케줄 컨텐츠 ID", required = true)
		@PathVariable String contentId
	);

	@Operation(
		summary = "스케줄 목록 조회", 
		description = "기간, 부두, 선석 조건으로 스케줄 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "스케줄 목록 조회 성공",
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
					    "scheduleId": "schedule-2025-10-001",
					    "pierName": "1부두",
					    "berthName": "A-1",
					    "startDt": "2025-10-13",
					    "endDt": "2025-10-20",
					    "timeTableWrappers": []
					  }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<ScheduleListResponse>> listSchedules(
		@Parameter(description = "조회 시작일", required = true, example = "2025-10-13")
		@RequestParam LocalDate startDate,
		@Parameter(description = "조회 종료일", required = true, example = "2025-10-20")
		@RequestParam LocalDate endDate,
		@Parameter(description = "부두명", required = true, example = "1부두")
		@RequestParam String pier,
		@Parameter(description = "선석명", required = true, example = "A-1")
		@RequestParam String berth
	);

	@Operation(
		summary = "스케줄 업로드", 
		description = "Excel 파일로 스케줄을 일괄 업로드합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "업로드 성공"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 파일 형식",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "파일 형식 오류",
					value = """
					{
					  "isSuccess": false,
					  "code": "COMMON_400",
					  "message": "지원하지 않는 파일 형식입니다."
					}
					"""
				)
			)
		)
	})
	void upload(
		@Parameter(description = "업로드할 Excel 파일", required = true)
		@RequestPart("file") MultipartFile file,
		@Parameter(description = "스케줄 생성 정보", required = true)
		@RequestPart("request") ScheduleCreateRequest request
	) throws IOException;

	@Operation(
		summary = "선석 자동 배정",
		description = "스케줄 ID로 선박들에 선석을 알고리즘을 통해 자동으로 배정합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", 
			description = "선석 배정 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "배정 성공",
					value = """
					{
					  "isSuccess": true,
					  "code": "COMMON200",
					  "message": "성공입니다.",
					  "result": [
					    {
					      "vesselName": "HARMONY ACE",
					      "cargoType": "컨테이너",
					      "pierName": "1부두",
					      "berthName": "A-1",
					      "eta": "2025-10-13T14:00:00",
					      "etd": "2025-10-13T16:00:00"
					    }
					  ]
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "스케줄 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "스케줄 없음",
					value = """
					{
					  "isSuccess": false,
					  "code": "SCHEDULE_404",
					  "message": "해당 스케줄을 찾을 수 없습니다."
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse<List<AssignedShipResponse>>> assign(
		@Parameter(description = "스케줄 ID", required = true)
		@PathVariable String scheduleId
	);

}

