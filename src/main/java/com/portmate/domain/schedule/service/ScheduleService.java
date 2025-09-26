package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.entity.Schedule;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ScheduleService {
    Schedule uploadExcel(MultipartFile file, ScheduleCreateRequest request) throws IOException;
}
