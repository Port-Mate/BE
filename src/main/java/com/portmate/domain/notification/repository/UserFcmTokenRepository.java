package com.portmate.domain.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.portmate.domain.notification.domain.UserFcmToken;
import com.portmate.domain.notification.repository.custom.CustomUserFcmTokenRepository;

@Repository
public interface UserFcmTokenRepository extends MongoRepository<UserFcmToken, String>, CustomUserFcmTokenRepository {
}
