package com.portmate.domain.schedule.repository;

import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.repository.custom.CustomScheduleRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String>, CustomScheduleRepository {
}
