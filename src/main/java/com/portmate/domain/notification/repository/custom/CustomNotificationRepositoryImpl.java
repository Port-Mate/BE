package com.portmate.domain.notification.repository.custom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.portmate.domain.notification.domain.Notification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

	private final MongoTemplate mongoTemplate;

	@Override
	public List<Notification> findByUserIdAndIsReadFalse(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId)
			.and("isRead").is(false));
		return mongoTemplate.find(query, Notification.class);
	}

	@Override
	public Optional<Notification> findByNotificationsId(String notificationsId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(notificationsId));
		return Optional.ofNullable(mongoTemplate.findOne(query, Notification.class));
	}

	@Override
	public List<Notification> findAllByIds(List<String> ids) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids));
		return mongoTemplate.find(query, Notification.class);
	}
}
