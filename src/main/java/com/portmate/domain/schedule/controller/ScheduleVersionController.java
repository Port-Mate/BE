package com.portmate.domain.schedule.controller;

import com.portmate.domain.schedule.dto.*;
import com.portmate.domain.schedule.service.ScheduleVersionService;
import com.portmate.global.auth.CustomUserDetails;
import com.portmate.global.response.ApiResponse;
import com.portmate.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule-versions")
public class ScheduleVersionController {
    
    private final ScheduleVersionService scheduleVersionService;
    

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<ScheduleValidateResponse>> validateSchedule(
            @RequestBody ScheduleValidateRequest request
    ) {
        ScheduleValidateResponse response = scheduleVersionService.validateScheduleModification(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ScheduleModifyResponse>> saveScheduleModification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScheduleModifyRequest request
    ) {
        ScheduleModifyResponse response = scheduleVersionService.saveScheduleModification(
                userDetails.getUserId(),
                userDetails.getUsername(),
                request
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<ApiResponse<ScheduleVersionResponse>> getScheduleVersion(
            @PathVariable String versionId
    ) {
        ScheduleVersionResponse response = scheduleVersionService.getScheduleVersion(versionId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<java.util.List<ScheduleVersionResponse>>>> getAllVersions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<java.util.List<ScheduleVersionResponse>> response = scheduleVersionService.getAllVersions(page, size);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    
    @PostMapping("/{versionId}/approve")
    public ResponseEntity<ApiResponse<ReviewActionResponse>> approveVersion(
            @PathVariable String versionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewActionRequest request
    ) {
        ReviewActionResponse response = scheduleVersionService.approveVersion(
                versionId, 
                userDetails.getEmail(),
                request
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
    
    @PostMapping("/{versionId}/reject")
    public ResponseEntity<ApiResponse<ReviewActionResponse>> rejectVersion(
            @PathVariable String versionId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewActionRequest request
    ) {
        ReviewActionResponse response = scheduleVersionService.rejectVersion(
                versionId, 
                userDetails.getEmail(),
                request
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
    
    @PostMapping("/{versionId}/apply")
    public ResponseEntity<ApiResponse<ScheduleApplyResponse>> applyVersionToSchedule(
            @PathVariable String versionId
    ) {
        ScheduleApplyResponse response = scheduleVersionService.applyVersionToSchedule(versionId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}
