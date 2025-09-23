package com.portmate.domain.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portmate.domain.notification.domain.UserFcmToken;
import com.portmate.domain.notification.dto.FcmTokenRequest;
import com.portmate.domain.notification.repository.UserFcmTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFcmTokenService {
	private final UserFcmTokenRepository userFcmTokenRepository;

	@Transactional
	public void saveToken(String userId, FcmTokenRequest req) {
		userFcmTokenRepository.findByUserIdAndTokenAndPlatform(userId, req.token(),req.platform())
			.ifPresentOrElse(userFcmToken -> {},
				() -> {
					UserFcmToken token = UserFcmToken.builder()
						.userId(userId)
						.token(req.token())
						.platform(req.platform())
						.build();
					userFcmTokenRepository.save(token);
				}
			);

	}
}
