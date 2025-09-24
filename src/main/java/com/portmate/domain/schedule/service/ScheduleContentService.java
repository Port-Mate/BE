package com.portmate.domain.schedule.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ScheduleContentService {
    void uploadExcel(MultipartFile file) throws IOException;
}
