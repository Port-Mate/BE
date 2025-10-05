package com.portmate.domain.schedule.controller;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.ScheduleDetailResponse;
import com.portmate.domain.schedule.service.ScheduleService;
import com.portmate.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<ScheduleDetailResponse>> getByScheduleId(@PathVariable String scheduleId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(scheduleService.queryByScheduleId(scheduleId)));
    }

    @PostMapping
    public void upload(@RequestPart("file") MultipartFile file,
                       @RequestPart("request") ScheduleCreateRequest request) throws IOException {
        scheduleService.uploadExcel(file, request);
    }
}
