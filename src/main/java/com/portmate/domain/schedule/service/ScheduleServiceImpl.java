package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.exception.FileNotSupportException;
import com.portmate.domain.schedule.repository.ScheduleRepository;
import com.portmate.domain.schedule.vo.ScheduleContent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.portmate.domain.schedule.util.ExcelUtil.isNotExcel;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;

    @Override
    public Schedule uploadExcel(MultipartFile file, ScheduleCreateRequest request) throws IOException {
        if (isNotExcel(file)) {
            throw new FileNotSupportException();
        }

        Schedule schedule = Schedule.create(request);

        List<ScheduleContent> result = parseExcel(file);
        schedule.addScheduleContents(result);
        scheduleRepository.save(schedule);
        return schedule;
    }

    private List<ScheduleContent> parseExcel(MultipartFile file) throws IOException {
        List<ScheduleContent> scheduleContents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // 헤더 읽기
            List<String> headers = csvParser.getHeaderNames();
            for (String header : headers) {
                scheduleContents.add(ScheduleContent.create(header, new ArrayList<>()));
            }

            // 데이터 행 읽기
            for (CSVRecord record : csvParser) {
                for (int i = 0; i < headers.size(); i++) {
                    scheduleContents.get(i).getData().add(record.get(i));
                }
            }
        }

        return scheduleContents;
    }
}
