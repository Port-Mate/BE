package com.portmate.domain.schedule.repository;

import com.portmate.domain.schedule.entity.ScheduleMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleMetaDataRepository extends MongoRepository<ScheduleMetaData, String> {
}
