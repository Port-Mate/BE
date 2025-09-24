package com.portmate.domain.schedule.controller;

import com.portmate.domain.schedule.service.ScheduleContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleContentService scheduleContentService;

    @PostMapping
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        scheduleContentService.uploadExcel(file);
    }
}
