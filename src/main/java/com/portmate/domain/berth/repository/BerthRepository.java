package com.portmate.domain.berth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portmate.domain.berth.entity.Berth;
import com.portmate.domain.berth.repository.custom.CustomBerthRepository;

@Repository
public interface BerthRepository extends MongoRepository<Berth, String> , CustomBerthRepository {
}
