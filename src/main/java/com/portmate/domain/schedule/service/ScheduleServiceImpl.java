package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.ScheduleListResponse;
import com.portmate.domain.schedule.dto.response.TimeTableHoverResponse;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.exception.FileNotSupportException;
import com.portmate.domain.schedule.exception.ScheduleInvalidEndException;
import com.portmate.domain.schedule.exception.ScheduleInvalidStartException;
import com.portmate.domain.schedule.exception.ScheduleNotFoundException;
import com.portmate.domain.schedule.repository.ScheduleRepository;
import com.portmate.domain.schedule.vo.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        List<ScheduleContent> result = parseExcel(file, request);
        schedule.addScheduleContents(result);
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Override
    public TimeTableHoverResponse queryContentByContentId(String contentId) {
        ScheduleContent schedule = scheduleRepository.findByContentId(contentId);
        return TimeTableHoverResponse.from(schedule);
    }

    @Override
    public ScheduleListResponse queryByListParams(LocalDate startDate, LocalDate endDate, String pier, String berth) {
        Schedule schedule = scheduleRepository.findByListParams(startDate, endDate, pier, berth);
        List<TimeTableWrapper> items = toTimeTable(schedule);
        return ScheduleListResponse.from(schedule, items);
    }

    private List<TimeTableWrapper> toTimeTable(Schedule schedule) {
        Map<String, List<TimeTableContent>> groupedByDate = new TreeMap<>();

        for (ScheduleContent sc : schedule.getScheduleContents()) {

            // ETA
            if (sc.getEta() != null) {
                String etaDate = sc.getEta().substring(0, 10);
                groupedByDate
                        .computeIfAbsent(etaDate, k -> new ArrayList<>())
                        .add(TimeTableContent.from(sc));
            }

            // ETD
            if (sc.getEtd() != null) {
                String etdDate = sc.getEtd().substring(0, 10);
                groupedByDate
                        .computeIfAbsent(etdDate, k -> new ArrayList<>())
                        .add(TimeTableContent.from(sc));
            }
        }

        return groupedByDate.entrySet().stream()
                .map(entry -> new TimeTableWrapper(entry.getKey(), entry.getValue()))
                .toList();
    }


    private List<ScheduleContent> parseExcel(MultipartFile file, ScheduleCreateRequest request) throws IOException {
        List<ScheduleContent> scheduleContents = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String etaStr = record.get("ETA (입항 예정)").substring(0, 10);
                String etdStr = record.get("ETD (출항 예정)").substring(0, 10);

                LocalDate eta = LocalDate.parse(etaStr, formatter);
                LocalDate etd = LocalDate.parse(etdStr, formatter);

                if(eta.isBefore(request.startDt())) {
                    throw new ScheduleInvalidStartException();
                }

                if(etd.isAfter(request.endDt())) {
                    throw new ScheduleInvalidEndException();
                }
                scheduleContents.add(ScheduleContent.from(record));
            }
        }

        return scheduleContents;
    }
}
