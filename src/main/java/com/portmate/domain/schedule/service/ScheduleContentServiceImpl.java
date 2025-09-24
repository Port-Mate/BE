package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.entity.ScheduleContent;
import com.portmate.domain.schedule.exception.FileNotSupportException;
import com.portmate.domain.schedule.repository.ScheduleContentRepository;
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

import static com.portmate.domain.schedule.util.ExcelUtil.*;

@Service
@RequiredArgsConstructor
public class ScheduleContentServiceImpl implements ScheduleContentService{
    private final ScheduleContentRepository scheduleContentRepository;


    @Override
    public void uploadExcel(MultipartFile file) throws IOException {
        if (isNotExcel(file)) {
            throw new FileNotSupportException();
        }
        List<ScheduleContent> result = parseExcel(file);
        scheduleContentRepository.saveAll(result);
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
