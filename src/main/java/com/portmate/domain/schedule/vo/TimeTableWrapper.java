package com.portmate.domain.schedule.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableWrapper {
    private String date;
    private List<TimeTableContent> items;
}
