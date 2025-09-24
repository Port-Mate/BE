package com.portmate.domain.schedule.entity;

import com.portmate.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "schedule_meta_data")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMetaData extends BaseEntity {
    @Id
    private String scheduleId;
    private String title;
    private LocalDate startDt;
    private LocalDate endDt;
}
