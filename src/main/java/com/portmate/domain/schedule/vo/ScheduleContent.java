package com.portmate.domain.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleContent{
    @Id
    private String id;
    private String column;
    private List<String> data;

    public static ScheduleContent create(String column, List<String> data){
        return ScheduleContent.builder()
                .id(UUID.randomUUID().toString())
                .column(column)
                .data(data)
                .build();
    }
}
