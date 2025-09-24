package com.portmate.domain.pier.repository.custom;

import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.portmate.domain.pier.entity.Pier;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomPierRepositoryImpl implements CustomPierRepository
{
	private final MongoTemplate mongoTemplate;
	@Override
	public Optional<Pier> findByPierName(String pierName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("pierName").is(pierName));

		return Optional.ofNullable(mongoTemplate.findOne(query, Pier.class));
	}
}
