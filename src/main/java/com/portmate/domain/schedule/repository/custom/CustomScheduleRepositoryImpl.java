package com.portmate.domain.schedule.repository.custom;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.vo.ScheduleContent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public ScheduleContent findByContentId(String contentId) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("scheduleContents._id").is(contentId)),
                Aggregation.unwind("scheduleContents"),
                Aggregation.match(Criteria.where("scheduleContents._id").is(contentId)),
                Aggregation.replaceRoot("scheduleContents")
        );

        return mongoTemplate.aggregate(agg, "schedule", ScheduleContent.class)
                .getUniqueMappedResult();
    }

    @Override
    public Schedule findByListParams(LocalDate startDate, LocalDate endDate, String port ,String pier, String berth) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.plusDays(1).atStartOfDay(); // endDate의 하루 끝까지 포함

        // LocalDate를 Date로 변환
        java.util.Date startDateConverted = java.sql.Date.valueOf(startDate);
        java.util.Date endDateConverted = java.sql.Date.valueOf(endDate.plusDays(1));

        Query query = new Query(
                new Criteria()
                        .andOperator(
                                Criteria.where("startDt").gte(startDateConverted),
                                Criteria.where("endDt").lt(endDateConverted),
                                Criteria.where("port").is(port),
                                Criteria.where("scheduleContents.pier").is(pier),
                                Criteria.where("scheduleContents.berth").is(berth)
                        )
        );

        return mongoTemplate.findOne(query, Schedule.class);
    }
}
