package com.portmate.domain.schedule.entity;

import com.portmate.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "schedule_contents")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleContent extends BaseEntity {
    @Id
    private String scheduleContentId;
    private String column;
    private List<String> data;

    public static ScheduleContent create(String column, List<String> data){
        return ScheduleContent.builder()
                .column(column)
                .data(data)
                .build();
    }
}
