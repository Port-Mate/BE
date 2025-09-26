package com.portmate.domain.schedule.controller;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public void upload(@RequestPart("file") MultipartFile file,
                       @RequestPart("request") ScheduleCreateRequest request) throws IOException {
        scheduleService.uploadExcel(file, request);
    }
}
