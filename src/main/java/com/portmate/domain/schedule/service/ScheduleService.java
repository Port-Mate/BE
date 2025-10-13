package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.ScheduleListResponse;
import com.portmate.domain.schedule.dto.response.TimeTableHoverResponse;
import com.portmate.domain.schedule.entity.Schedule;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface ScheduleService {
    Schedule uploadExcel(MultipartFile file, ScheduleCreateRequest request) throws IOException;
    TimeTableHoverResponse queryContentByContentId(String contentId);
    ScheduleListResponse queryByListParams(LocalDate startDate, LocalDate endDate, String port,String pier, String berth);
}
