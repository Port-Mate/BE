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

        
        Query query = new Query(
                new Criteria()
                        .andOperator(
                                Criteria.where("startDt").lte(endDate),
                                Criteria.where("endDt").gte(startDate),
                                Criteria.where("port").is(port),
                                Criteria.where("scheduleContents").elemMatch(
                                    new Criteria()
                                        .andOperator(
                                            Criteria.where("pier").is(pier),
                                            Criteria.where("berth").is(berth)
                                        )
                                )
                        )
        );
        
        Schedule result = mongoTemplate.findOne(query, Schedule.class);
        return result;
    }
}
