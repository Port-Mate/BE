package com.portmate.domain.schedule.repository.custom;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.ScheduleContent;

import java.time.LocalDate;
import java.util.Date;

public interface CustomScheduleRepository {
    ScheduleContent findByContentId(String contentId);
    Schedule findByListParams(LocalDate startDate, LocalDate endDate, String pier, String berth);
}
