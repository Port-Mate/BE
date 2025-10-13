package com.portmate.domain.schedule.controller;

import com.portmate.docs.ScheduleControllerDocs;
import com.portmate.domain.berth.service.BerthAssignmentService;
import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.AssignedShipResponse;
import com.portmate.domain.schedule.dto.response.ScheduleListResponse;
import com.portmate.domain.schedule.dto.response.TimeTableHoverResponse;
import com.portmate.domain.schedule.service.ScheduleService;
import com.portmate.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController implements ScheduleControllerDocs {
    private final ScheduleService scheduleService;
    private final BerthAssignmentService berthAssignmentService;

    @GetMapping("/{contentId}")
    public ResponseEntity<ApiResponse<TimeTableHoverResponse>> getByScheduleId(@PathVariable String contentId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(scheduleService.queryContentByContentId(contentId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ScheduleListResponse>> listSchedules(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String port,
            @RequestParam String pier,
            @RequestParam String berth) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                scheduleService.queryByListParams(startDate, endDate, port, pier, berth))
        );
    }

    @PostMapping
    public void upload(@RequestPart("file") MultipartFile file,
                       @RequestPart("request") ScheduleCreateRequest request) throws IOException {
        scheduleService.uploadExcel(file, request);
    }

    @PostMapping("/{scheduleId}/assign")
    public ResponseEntity<ApiResponse<List<AssignedShipResponse>>> assign(@PathVariable String scheduleId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(berthAssignmentService.assignByScheduleId(scheduleId)));
    }
}
