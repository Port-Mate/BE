package com.portmate;

import com.portmate.domain.schedule.entity.ScheduleMetaData;
import com.portmate.domain.schedule.repository.ScheduleMetaDataRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest
public class ScheduleMetaDataServiceTest {
    @Autowired
    private ScheduleMetaDataRepository scheduleMetaDataRepository;

    List<String> createdIds = new ArrayList<>();

    @Test
    public void 일정표의_메타데이터를_등록한다() {
        var meta = List.of(
                new ScheduleMetaData("Schedule 1", "일정 1", LocalDate.now(), LocalDate.now().plusDays(7)),
                new ScheduleMetaData("Schedule 2", "일정 2", LocalDate.now(), LocalDate.now().plusDays(7)),
                new ScheduleMetaData("Schedule 3", "일정 3", LocalDate.now(), LocalDate.now().plusDays(7))
        );

        scheduleMetaDataRepository.saveAll(meta);
        createdIds.add("Schedule 1");
        createdIds.add("Schedule 2");
        createdIds.add("Schedule 3");

        var all = scheduleMetaDataRepository.findAll();

        assertThat(all).hasSize(3);
        assertThat(all).isNotNull();
        assertThat(all)
                .extracting(ScheduleMetaData::getScheduleId, ScheduleMetaData::getTitle)
                .containsExactlyInAnyOrder(
                        tuple("Schedule 1", "일정 1"),
                        tuple("Schedule 2", "일정 2"),
                        tuple("Schedule 3", "일정 3")
                );
    }

    @AfterEach
    void cleanUp(){
        scheduleMetaDataRepository.deleteAllById(createdIds);
        createdIds.clear();
    }
}
