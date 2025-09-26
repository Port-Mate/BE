package com.portmate.domain.schedule.entity;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.vo.ScheduleContent;
import com.portmate.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "schedule")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {
    @Id
    private String scheduleId;
    private String portName;
    private LocalDate startDt;
    private LocalDate endDt;
    private List<ScheduleContent> scheduleContents;


    public static Schedule create(ScheduleCreateRequest request){
        return Schedule.builder()
                .portName(request.portName())
                .startDt(request.startDt())
                .endDt(request.endDt())
                .build();
    }

    public void addScheduleContents(List<ScheduleContent> scheduleContents){
        this.scheduleContents = scheduleContents;
    }

}
