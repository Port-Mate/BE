package com.portmate.domain.schedule.repository;

import com.portmate.domain.schedule.entity.ScheduleContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleContentRepository extends MongoRepository<ScheduleContent, String> {
}
