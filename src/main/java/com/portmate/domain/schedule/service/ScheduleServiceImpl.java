package com.portmate.domain.schedule.service;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.dto.response.ScheduleDetailResponse;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.exception.FileNotSupportException;
import com.portmate.domain.schedule.exception.ScheduleInvalidEndException;
import com.portmate.domain.schedule.exception.ScheduleInvalidStartException;
import com.portmate.domain.schedule.exception.ScheduleNotFoundException;
import com.portmate.domain.schedule.repository.ScheduleRepository;
import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.domain.schedule.vo.TimeTableContent;
import com.portmate.domain.schedule.vo.TimeTableWrapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public ScheduleDetailResponse queryByScheduleId(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        List<TimeTableWrapper> items = toTimeTable(schedule);
        return ScheduleDetailResponse.from(schedule, items);
    }

    private List<TimeTableWrapper> toTimeTable(Schedule schedule) {
        Map<String, List<TimeTableContent>> groupedByDate = new TreeMap<>();

        for (ScheduleContent sc : schedule.getScheduleContents()) {

            // ETA
            if (sc.getEta() != null) {
                String etaDate = sc.getEta().substring(0, 10);
                groupedByDate
                        .computeIfAbsent(etaDate, k -> new ArrayList<>())
                        .add(TimeTableContent.builder()
                                .no(sc.getNo())
                                .vesselName(sc.getVesselName())
                                .imoOrCallSign(sc.getImoOrCallSign())
                                .voyageNo(sc.getVoyageNo())
                                .etaTime(sc.getEta().substring(11)) // HH:mm
                                .fromPort(sc.getFromPort())
                                .nextPort(sc.getNextPort())
                                .cargoType(sc.getCargoType())
                                .tonnage(sc.getTonnage())
                                .flag(sc.getFlag())
                                .remark(sc.getRemark())
                                .build());
            }

            // ETD
            if (sc.getEtd() != null) {
                String etdDate = sc.getEtd().substring(0, 10);
                groupedByDate
                        .computeIfAbsent(etdDate, k -> new ArrayList<>())
                        .add(TimeTableContent.builder()
                                .no(sc.getNo())
                                .vesselName(sc.getVesselName())
                                .imoOrCallSign(sc.getImoOrCallSign())
                                .voyageNo(sc.getVoyageNo())
                                .etdTime(sc.getEtd().substring(11)) // HH:mm
                                .fromPort(sc.getFromPort())
                                .nextPort(sc.getNextPort())
                                .cargoType(sc.getCargoType())
                                .tonnage(sc.getTonnage())
                                .flag(sc.getFlag())
                                .remark(sc.getRemark())
                                .build());
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
