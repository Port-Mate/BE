package com.portmate.domain.notification.repository.custom;

import java.util.List;
import java.util.Optional;

import com.portmate.domain.notification.domain.UserFcmToken;

public interface CustomUserFcmTokenRepository {
	Optional<UserFcmToken> findByUserIdAndTokenAndPlatform(String userId, String token,String platform);
	List<UserFcmToken> findByUserId(String userId);
	void deleteByUserIdAndToken(String userId, String token);
}
