package com.portmate.domain.pier.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portmate.domain.pier.entity.Pier;
import com.portmate.domain.pier.repository.custom.CustomPierRepository;

@Repository
public interface PierRepository extends MongoRepository<Pier,String> , CustomPierRepository {
}
