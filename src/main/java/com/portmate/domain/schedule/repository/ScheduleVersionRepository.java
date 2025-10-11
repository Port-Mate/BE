package com.portmate.domain.schedule.repository;

import com.portmate.domain.schedule.entity.ScheduleVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleVersionRepository extends MongoRepository<ScheduleVersion, String> {
    

    Optional<ScheduleVersion> findByOriginalScheduleIdAndStatus(String originalScheduleId, ScheduleVersion.VersionStatus status);
    

    List<ScheduleVersion> findByOriginalScheduleIdOrderByCreatedAtDesc(String originalScheduleId);
    

    List<ScheduleVersion> findByCreatedByAndStatus(String createdBy, ScheduleVersion.VersionStatus status);
}
