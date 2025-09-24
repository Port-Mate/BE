package com.portmate.domain.notification.repository.custom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.portmate.domain.notification.domain.UserFcmToken;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomUserFcmTokenRepositoryImpl implements CustomUserFcmTokenRepository {

	private final MongoTemplate mongoTemplate;

	@Override
	public Optional<UserFcmToken> findByUserIdAndTokenAndPlatform(String userId, String token, String platform) {
		Query query = new Query(Criteria.where("userId").is(userId)
			.and("token").is(token)
			.and("platform").is(platform));

		UserFcmToken result=mongoTemplate.findOne(query, UserFcmToken.class);
		return Optional.ofNullable(result);
	}

	@Override
	public List<UserFcmToken> findByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));

		return mongoTemplate.find(query, UserFcmToken.class);
	}

	@Override
	public void deleteByUserIdAndToken(String userId, String token) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId)
			.and("token").is(token));

		mongoTemplate.remove(query, UserFcmToken.class);
	}
}
